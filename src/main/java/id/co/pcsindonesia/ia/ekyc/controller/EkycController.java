package id.co.pcsindonesia.ia.ekyc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.ProfileCommadDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.UserCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.vida.VidaDemogCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.GetOrCreateUserDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.GlobalDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.UserDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.*;
import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
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
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class EkycController {

    private final ProfileQueryService profileQueryService;
    private final EkycVidaCommandService ekycVidaCommandService;
    private final UserCommandService userCommandService;

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
    public ResponseEntity<GlobalDto<UserDto>> registerOcr(@Valid @RequestBody OcrCommandDto body) throws JsonProcessingException, InterruptedException {
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

    @Operation(summary = "Register by Form", security = @SecurityRequirement(name = "bearerAuth"))
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

    private ResponseEntity<GlobalDto<UserDto>> getGlobalDtoResponseEntity(User user) throws JsonProcessingException, InterruptedException {
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

}
