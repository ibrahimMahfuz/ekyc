package id.co.pcsindonesia.ia.ekyc.controller;

import id.co.pcsindonesia.ia.ekyc.dto.command.ProfileCommadDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileDto;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
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
    public ResponseEntity<ProfileDto> getProfiles(@RequestBody ProfileCommadDto body){
        ProfileDto profileByTerminalId = profileQueryService.getProfileByTerminalId(body.getTerminalId());
        return new ResponseEntity<>(profileByTerminalId, HttpStatus.OK);
    }

}
