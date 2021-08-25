package id.co.pcsindonesia.ia.ekyc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.ProfileCommadDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.UserCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.vida.VidaDemogCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.*;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.*;
import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycAsliRiCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
import id.co.pcsindonesia.ia.ekyc.switcher.EkycSwitcher;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServiceUnavailableException;
import id.co.pcsindonesia.ia.ekyc.util.properties.EkycVendorProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class EkycController {

    private final ProfileQueryService profileQueryService;
    private final EkycVidaCommandService ekycVidaCommandService;
    private final EkycAsliRiCommandService ekycAsliRiCommandService;
    private final UserCommandService userCommandService;
    private final EkycSwitcher ekycSwitcher;
    private final EkycVendorProperty ekycVendorProperty;

    private ResponseEntity<GlobalDto<UserDto>>
    getGlobalDtoResponseEntity(User user)
            throws JsonProcessingException,
            InterruptedException {
        GetOrCreateUserDto getOrCreateUserDto = userCommandService.getOrCreate(user);
        User orCreate = getOrCreateUserDto.getUser();
        UserDto userQueryDto = UserDto.builder()
                .nik(orCreate.getNik())
                .name(orCreate.getName())
                .dob(orCreate.getDob().toString())
                .pob(orCreate.getPob())
                .verified(orCreate.getVerified())
                .build();
        if (!getOrCreateUserDto.getIsGet()){
            VidaGlobalDto<VidaTransactionDto> vidaTransactionDtoVidaGlobalDto = ekycVidaCommandService.demogLite(VidaDemogCommandDto.builder()
                    .nik(orCreate.getNik())
                    .fullName(orCreate.getName())
                    .dob(orCreate.getDob().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .phoneNo(orCreate.getPhoneNumber())
                    .email(orCreate.getEmail())
                    .build());
            VidaStatusDto<VidaDemogDto> status = ekycVidaCommandService.getStatus(vidaTransactionDtoVidaGlobalDto.getData().getTransactionId(), VidaDemogDto.class);
            if (!status.getData().getResult().getMatch()){
                userQueryDto.setVerified(false);
                orCreate.setVerified(false);
            }else {
                userQueryDto.setVerified(true);
                orCreate.setVerified(true);
            }
            userCommandService.updateUser(orCreate);
        }
        return new ResponseEntity<>(GlobalDto.<UserDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(userQueryDto)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/profiles")
    public ResponseEntity<GlobalDto<ProfileDto>> getProfiles(@RequestBody ProfileCommadDto body){
        ProfileDto profileByTerminalId = profileQueryService.getProfileByTerminalId(body.getTerminalId());
        return new ResponseEntity<>(GlobalDto.<ProfileDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(profileByTerminalId)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Register by OCR", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/register/ocr")
    public ResponseEntity<GlobalDto<UserDto>> registerOcr(@Valid @RequestBody OcrCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        if (ekycSwitcher.getOcrType(service).equals(ekycVendorProperty.getVida())){
            VidaGlobalDto<VidaTransactionDto> ocr = ekycVidaCommandService.ocr(body);
            VidaStatusDto<VidaStatusOcrDto> status = ekycVidaCommandService.getStatus(ocr.getData().getTransactionId(), VidaStatusOcrDto.class);
            LocalDate ldate = LocalDate.parse(status.getData().getResult().getTanggalLahir(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            User user = User.builder()
                    .nik(status.getData().getResult().getNik())
                    .name(status.getData().getResult().getNama())
                    .dob(ldate)
                    .pob(status.getData().getResult().getTempatLahir())
                    .verified(true)
                    .build();
            return getGlobalDtoResponseEntity(user);
        }else {
            ekycAsliRiCommandService.ocr(null);
            return null;
        }
    }

    @Operation(summary = "OCR Only", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/ocr")
    public ResponseEntity<GlobalDto<VidaStatusOcrDto>> ocr(@Valid @RequestBody OcrCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        if (ekycSwitcher.getOcrType(service).equals(ekycVendorProperty.getVida())){
            VidaGlobalDto<VidaTransactionDto> ocr = ekycVidaCommandService.ocr(body);
            VidaStatusDto<VidaStatusOcrDto> status = ekycVidaCommandService.getStatus(ocr.getData().getTransactionId(), VidaStatusOcrDto.class);
            return new ResponseEntity<>(GlobalDto.<VidaStatusOcrDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(status.getData().getResult())
                    .build(), HttpStatus.OK);
        }else {
            ekycAsliRiCommandService.ocr(null);
            return null;
        }
    }

    @Operation(summary = "Register by Form", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/register/form")
    public ResponseEntity<GlobalDto<UserDto>> registerForm(@Valid @RequestBody UserCommandDto userCommandDto) throws JsonProcessingException, InterruptedException {
        User user = User.builder()
                .nik(userCommandDto.getNik())
                .name(userCommandDto.getName())
                .dob(userCommandDto.getDob())
                .pob(userCommandDto.getPob())
                .verified(false)
                .build();
        return getGlobalDtoResponseEntity(user);
    }

    @Operation(summary = "Liveness and facematch", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/compositions/liveness-and-facematch")
    public ResponseEntity<GlobalDto<VidaFmHandlerDto>> livenessAndFaceMatch(@Valid @RequestBody LnFmCommandDto body) throws JsonProcessingException, InterruptedException {
        // -- skip -- ekycVidaCommandService.liveness(body);
        VidaGlobalDto<VidaTransactionDto> vidaTransactionDtoVidaGlobalDto = ekycVidaCommandService.faceMatch(body);
        VidaStatusDto<VidaFaceMatchDto> status = ekycVidaCommandService.getStatus(vidaTransactionDtoVidaGlobalDto.getData().getTransactionId(), VidaFaceMatchDto.class);
        VidaFmHandlerDto vidaFmHandlerDto = ekycVidaCommandService.completeIdHandler(status);
        return new ResponseEntity<>(GlobalDto.<VidaFmHandlerDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(vidaFmHandlerDto)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Extra Tax", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/extra-taxes")
    public ResponseEntity<GlobalDto<AsliRiGlobalDto<AsliRiExtraTaxDto>>> livenessAndFaceMatch(@Valid @RequestBody AsliRiExtraTaxCommandDto body) throws JsonProcessingException, InterruptedException {
        AsliRiGlobalDto<AsliRiExtraTaxDto> asliRiExtraTaxDtoAsliRiGlobalDto = ekycAsliRiCommandService.extraTaxVerification(body);
        return new ResponseEntity<>(GlobalDto.<AsliRiGlobalDto<AsliRiExtraTaxDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(asliRiExtraTaxDtoAsliRiGlobalDto)
                .build(), HttpStatus.OK);
    }

}
