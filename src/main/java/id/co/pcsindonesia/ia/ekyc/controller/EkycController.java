package id.co.pcsindonesia.ia.ekyc.controller;

import id.co.pcsindonesia.ia.ekyc.dto.command.ProfileCommadDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.GlobalDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.GlobalErrorDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileDto;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class EkycController {

    private final ProfileQueryService profileQueryService;

    @PostMapping("/profiles")
    public ResponseEntity<GlobalDto<ProfileDto>> getProfiles(@RequestBody ProfileCommadDto body){
        ProfileDto profileByTerminalId = profileQueryService.getProfileByTerminalId(body.getTerminalId());
        return new ResponseEntity<>(GlobalDto.<ProfileDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(profileByTerminalId)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "testing Token", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/pro")
    public ResponseEntity<?> getPro(){
        return new ResponseEntity<>("hallo", HttpStatus.OK);
    }

}
