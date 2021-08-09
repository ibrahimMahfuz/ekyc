package id.co.pcsindonesia.ia.ekyc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.ProfileCommadDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.*;
import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<GlobalDto<OcrDto>> registerOcr(@RequestBody OcrCommandDto body) throws JsonProcessingException, InterruptedException {
        VidaOcrDto ocr = ekycVidaCommandService.ocr(body);
        VidaStatusDto<VidaStatusOcrDto> status = ekycVidaCommandService.getStatus(ocr.getTransactionId(), VidaStatusOcrDto.class);
        LocalDate ldate = LocalDate.parse(status.getResult().getTanggalLahir(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        User user = User.builder()
                .nik(status.getResult().getNik())
                .name(status.getResult().getNama())
                .dob(ldate)
                .pob(status.getResult().getTempatLahir())
                .verified(true)
                .build();
        return getGlobalDtoResponseEntity(user);
    }

    @Operation(summary = "Register by Form", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/register/form")
    public ResponseEntity<GlobalDto<OcrDto>> registerForm(@RequestBody User user){
        return getGlobalDtoResponseEntity(user);
    }

    private ResponseEntity<GlobalDto<OcrDto>> getGlobalDtoResponseEntity(User user) {
        User orCreate = userCommandService.getOrCreate(user);
        OcrDto ocrDto = new OcrDto();
        ocrDto.setIsVerified(orCreate.getVerified());
        return new ResponseEntity<>(GlobalDto.<OcrDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(ocrDto)
                .build(), HttpStatus.OK);
    }

}
