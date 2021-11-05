package id.co.pcsindonesia.ia.ekyc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.*;
import id.co.pcsindonesia.ia.ekyc.dto.query.*;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiOcrDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiPhoneDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.*;
import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycAsliRiCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycSimulationCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.impl.TempFallbackCommandService;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
import id.co.pcsindonesia.ia.ekyc.service.query.UserQueryService;
import id.co.pcsindonesia.ia.ekyc.switcher.EkycSwitcher;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServiceUnavailableException;
import id.co.pcsindonesia.ia.ekyc.util.properties.EkycVendorProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
@Slf4j
public class EkycController {

    private final ProfileQueryService profileQueryService;

    private final EkycVidaCommandService ekycVidaCommandService;
    private final EkycAsliRiCommandService ekycAsliRiCommandService;
    private final EkycSimulationCommandService ekycSimulationCommandService;

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    private final EkycSwitcher ekycSwitcher;
    private final EkycVendorProperty ekycVendorProperty;

    private final TempFallbackCommandService tempFallbackCommandService;

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
        if (!getOrCreateUserDto.getIsGet()) {
            VidaGlobalDto<VidaTransactionDto> vidaTransactionDtoVidaGlobalDto = ekycVidaCommandService.demog(DemogCommandDto.builder()
                    .nik(orCreate.getNik())
                    .fullName(orCreate.getName())
                    .dob(orCreate.getDob())
                    .phoneNo(orCreate.getPhoneNumber())
                    .email(orCreate.getEmail())
                    .build());
            VidaStatusDto<VidaDemogDto> status = ekycVidaCommandService.getStatus(vidaTransactionDtoVidaGlobalDto.getData().getTransactionId(), VidaDemogDto.class);
            if (!status.getData().getResult().getMatch()) {
                userQueryDto.setVerified(false);
                orCreate.setVerified(false);
            } else {
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

    @Operation(summary = "Get token and profile")
    @PostMapping("/profiles")
    public ResponseEntity<GlobalDto<ProfileDto>> profile(@RequestBody ProfileCommadDto body) {
        MDC.put("terminalId", String.valueOf(body.getTerminalId()));
        ProfileDto profileByTerminalId = profileQueryService.getProfileByTerminalId(body.getTerminalId());
        log.info("get profile = {}", profileByTerminalId.toString());
        MDC.remove("terminalId");
        return new ResponseEntity<>(GlobalDto.<ProfileDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(profileByTerminalId)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "OCR KTP and validate demog at once", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/composition/ocr-and-demogs")
    public ResponseEntity<GlobalDto<UserDto>> ocrAndDemog(@Valid @RequestBody OcrCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        if (ekycSwitcher.ocrType(service).equals(ekycVendorProperty.getVida())) {
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
        } else {
            ekycAsliRiCommandService.ocr(null);
            return null;
        }
    }

    @Operation(summary = "OCR KTP", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/ocrs")
    public ResponseEntity<GlobalDto<VidaStatusOcrDto>> ocr(
            @Valid @RequestBody OcrCommandDto body,
            Principal principal
    ) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        Long ocrType = ekycSwitcher.ocrType(service);
        if (ocrType.equals(ekycVendorProperty.getVida())) {
            VidaGlobalDto<VidaTransactionDto> ocr = ekycVidaCommandService.ocr(body);
            VidaStatusDto<VidaStatusOcrDto> status = ekycVidaCommandService.getStatus(ocr.getData().getTransactionId(), VidaStatusOcrDto.class);

            log.info("final result = {}", status.getData().getResult());
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<VidaStatusOcrDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(status.getData().getResult())
                    .build(), HttpStatus.OK);
        } else if (ocrType.equals(ekycVendorProperty.getAsliRi())) {
            AsliRiOcrDto ocr = ekycAsliRiCommandService.ocr(body);
            VidaStatusOcrDto ocrDto = VidaStatusOcrDto
                    .builder()
                    .nik(Long.parseLong(ocr.getNik()))
                    .provinsi(ocr.getProvinsi())
                    .kabupatenKota(ocr.getKota())
                    .golonganDarah(ocr.getGolDarah())
                    .agama(ocr.getAgama())
                    .alamat(ocr.getAlamat())
                    .berlakuHingga("SEUMUR HIDUP")
                    .kewarganegaraan(ocr.getKewarganegaraan())
                    .nama(ocr.getNama())
                    .pekerjaan(ocr.getPekerjaan())
                    .tempatLahir(ocr.getTempatLahir())
                    .kecamatan(ocr.getKecamatan())
                    .jenisKelamin(ocr.getJenisKelamin())
                    .rtRw(ocr.getRtRw())
                    .tanggalLahir(ocr.getTanggalLahir())
                    .statusPerkawinan(ocr.getStatusPerkawinan())
                    .kelurahanDesa(ocr.getKelurahanDesa())
                    .build();

            log.info("final result = {}", ocrDto);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<VidaStatusOcrDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(ocrDto)
                    .build(), HttpStatus.OK);
        } else {
            throw new VendorServiceUnavailableException("Your vendor or service are not registered in our system, please contact our admin");
        }
    }

    @Operation(summary = "Demog validation", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/demogs")
    public ResponseEntity<GlobalDto<MatchAndScoreDto>> demog(@Valid @RequestBody DemogCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        Long demogType = ekycSwitcher.demogType(service);
        Boolean nikVerified = userQueryService.isNikVerified(body.getNik());
        if (!nikVerified){
            if (demogType.equals(ekycVendorProperty.getVida())) {
                VidaGlobalDto<VidaTransactionDto> ocr = ekycVidaCommandService.demog(body);
                VidaStatusDto<VidaDemogDto> status = ekycVidaCommandService.getStatus(ocr.getData().getTransactionId(), VidaDemogDto.class);

                Double th = (body.getThreshold() / 10);
                log.info("final result = {}", status.getData().getResult());
                removeMdc();

                return new ResponseEntity<>(GlobalDto.<MatchAndScoreDto>builder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .result(new MatchAndScoreDto((status.getData().getResult().getScore() >= th), status.getData().getResult().getScore()))
                        .build(), HttpStatus.OK);
            } else if (demogType.equals(ekycVendorProperty.getAsliRi())) {
                ekycAsliRiCommandService.ocr(null);

                removeMdc();

                return null;
            } else if (demogType.equals(ekycVendorProperty.getSimulation())) {
                Boolean demog = ekycSimulationCommandService.demog(null);

                log.info("final result = {}", demog);
                removeMdc();

                return new ResponseEntity<>(GlobalDto.<MatchAndScoreDto>builder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .result(new MatchAndScoreDto(demog, null))
                        .build(), HttpStatus.OK);
            } else {
                throw new VendorServiceUnavailableException("Your vendor or service are not registered in our system, please contact our admin");
            }
        }else {

            log.info("final result from database = {}", nikVerified);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<MatchAndScoreDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(new MatchAndScoreDto(nikVerified, null))
                    .build(), HttpStatus.OK);
        }
    }

    private void removeMdc() {
        MDC.remove("terminalId");
        MDC.remove("serviceName");
        MDC.remove("vendorName");
    }

    @Operation(summary = "Register by form and validate demog", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/form-and-demogs")
    public ResponseEntity<GlobalDto<UserDto>> formAndDemog(@Valid @RequestBody UserCommandDto userCommandDto) throws JsonProcessingException, InterruptedException {
        User user = User.builder()
                .nik(userCommandDto.getNik())
                .name(userCommandDto.getName())
                .dob(userCommandDto.getDob())
                .pob(userCommandDto.getPob())
                .verified(false)
                .build();
        return getGlobalDtoResponseEntity(user);
    }

    @Operation(summary = "Liveness and validate face at once", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/composition/liveness-and-facematches")
    public ResponseEntity<GlobalDto<MatchAndScoreDto>> livenessAndFaceMatch(@Valid @RequestBody LnFmCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        Long facematchType = ekycSwitcher.facematchType(service);
        if (facematchType.equals(ekycVendorProperty.getVida())){
//            skip liveness
//            VidaGlobalDto<VidaHacknessDto> liveness = ekycVidaCommandService.liveness(body);
//            if (liveness.getData().getScore() > 0.99){
//                throw new BadRequestException("your photo suspect as hacked photo");
//            }
            VidaGlobalDto<VidaTransactionDto> vidaTransactionDtoVidaGlobalDto = ekycVidaCommandService.faceMatch(body);
            VidaStatusDto<VidaFaceMatchDto> status = ekycVidaCommandService.getStatus(vidaTransactionDtoVidaGlobalDto.getData().getTransactionId(), VidaFaceMatchDto.class);

            Double th = (body.getThreshold() / 10);
            log.info("final result = {}", status.getData().getResult());
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<MatchAndScoreDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(new MatchAndScoreDto((status.getData().getResult().getScore() >= th), status.getData().getResult().getScore()))
                    .build(), HttpStatus.OK);
        }else if (facematchType.equals(ekycVendorProperty.getAsliRi())){
            MatchAndScoreDto faceMatch = ekycAsliRiCommandService.faceMatch(body);

            log.info("final result = {}", faceMatch);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<MatchAndScoreDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(faceMatch)
                    .build(), HttpStatus.OK);
        }else if (facematchType.equals(ekycVendorProperty.getSimulation())){
            Boolean faceMatch = ekycSimulationCommandService.faceMatch(null);

            log.info("final result = {}", faceMatch);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<MatchAndScoreDto>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(new MatchAndScoreDto(faceMatch, null))
                    .build(), HttpStatus.OK);
        }else {
            throw new VendorServiceUnavailableException("Your vendor or service are not registered in our system, please contact our admin");
        }
    }

    @Operation(summary = "facematch vida 70, fallback asliRi", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/facematches/vida-fall-asliri")
    public ResponseEntity<GlobalDto<Boolean>> facematchVidaFallAsliRi(@Valid @RequestBody LnFmCommandDto body) throws JsonProcessingException, InterruptedException {
        if (tempFallbackCommandService.checkIfVida()){
            MDC.put("serviceName", "fallback face-match");
            MDC.put("vendorName", "vida");
            VidaGlobalDto<VidaTransactionDto> vidaTransactionDtoVidaGlobalDto = ekycVidaCommandService.faceMatch(body);
            if (vidaTransactionDtoVidaGlobalDto.getData().getTransactionId() != null) tempFallbackCommandService.addRequestToday();
            VidaStatusDto<VidaFaceMatchDto> status = ekycVidaCommandService.getStatus(vidaTransactionDtoVidaGlobalDto.getData().getTransactionId(), VidaFaceMatchDto.class);

            log.info("final result = {}", status.getData().getResult());
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<Boolean>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(status.getData().getResult().getMatch())
                    .build(), HttpStatus.OK);
        }else {
            MDC.put("serviceName", "fallback face-match");
            MDC.put("vendorName", "AsliRi");
            MatchAndScoreDto faceMatch = ekycAsliRiCommandService.faceMatch(body);

            log.info("final result = {}", faceMatch);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<Boolean>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(faceMatch.getMatch())
                    .build(), HttpStatus.OK);
        }
    }

        @Operation(summary = "Inclome validation", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/incomes")
    public ResponseEntity<GlobalDto<AsliRiGlobalDto<AsliRiExtraTaxDto>>> income(@Valid @RequestBody ExtraTaxCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        Long incomeType = ekycSwitcher.incomeType(service);
        if (incomeType.equals(ekycVendorProperty.getAsliRi())){
            AsliRiGlobalDto<AsliRiExtraTaxDto> asliRiExtraTaxDtoAsliRiGlobalDto = ekycAsliRiCommandService.extraTaxVerification(body);

            log.info("final result = {}", asliRiExtraTaxDtoAsliRiGlobalDto);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<AsliRiGlobalDto<AsliRiExtraTaxDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(asliRiExtraTaxDtoAsliRiGlobalDto)
                    .build(), HttpStatus.OK);
        }else if (incomeType.equals(ekycVendorProperty.getSimulation())){
            AsliRiGlobalDto<AsliRiExtraTaxDto> asliRiExtraTaxDtoAsliRiGlobalDto = ekycSimulationCommandService.extraTaxVerification();

            log.info("final result = {}", asliRiExtraTaxDtoAsliRiGlobalDto);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<AsliRiGlobalDto<AsliRiExtraTaxDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(asliRiExtraTaxDtoAsliRiGlobalDto)
                    .build(), HttpStatus.OK);
        }else {
            throw new VendorServiceUnavailableException("Your vendor or service are not registered in our system, please contact our admin");
        }
    }


    @Operation(summary = "Phone validation", security = @SecurityRequirement(name = "apikey"))
    @PostMapping("/phones")
    public ResponseEntity<GlobalDto<AsliRiGlobalDto<AsliRiPhoneDto>>> phone(@Valid @RequestBody PhoneCommandDto body, Principal principal) throws JsonProcessingException, InterruptedException {
        List<ProfileServiceDto> service = ekycSwitcher.getService(principal.getName());
        Long phoneType = ekycSwitcher.phoneType(service);
        if (phoneType.equals(ekycVendorProperty.getAsliRi())) {
            AsliRiGlobalDto<AsliRiPhoneDto> asliRiPhoneDtoAsliRiGlobalDto = ekycAsliRiCommandService.phoneVerification(body);

            log.info("final result = {}", asliRiPhoneDtoAsliRiGlobalDto);
            removeMdc();

            return new ResponseEntity<>(GlobalDto.<AsliRiGlobalDto<AsliRiPhoneDto>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(asliRiPhoneDtoAsliRiGlobalDto)
                    .build(), HttpStatus.OK);
        } else {
            throw new VendorServiceUnavailableException("Your vendor or service are not registered in our system, please contact our admin");
        }
    }

}
