package id.co.pcsindonesia.ia.ekyc.service.query.impl;

import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileServiceDto;
import id.co.pcsindonesia.ia.ekyc.entity.ProfileService;
import id.co.pcsindonesia.ia.ekyc.entity.Terminal;
import id.co.pcsindonesia.ia.ekyc.repository.ProfileServiceRepository;
import id.co.pcsindonesia.ia.ekyc.repository.TerminalRepository;
import id.co.pcsindonesia.ia.ekyc.service.query.ProfileQueryService;
import id.co.pcsindonesia.ia.ekyc.util.exception.DataNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final TerminalRepository terminalRepository;
    private final ProfileServiceRepository profileServiceRepository;

    @Override
    @Transactional
    public ProfileDto getProfileByTerminalId(Long terminalId) {
        Terminal terminal = terminalRepository.findById(terminalId).orElseThrow(DataNotFoundException::new);

        //set token
        String token =  UUID.randomUUID().toString();
        terminal.setToken(token);

        //set expired_at
        long expiredAt = LocalDate.now().plusDays(10).toEpochSecond(LocalTime.now(), UTC );
        terminal.setExpired_at(expiredAt);

        //get profile service
        List<ProfileService> allByProfile = profileServiceRepository.findAllByProfile(terminal.getProfile());

        List<ProfileServiceDto> profileServiceDtos =
                allByProfile.stream()
                        .map(profileService -> new ProfileServiceDto(profileService.getVendorService().getId(), profileService.getVendorService().getServiceCategory().getId())).collect(Collectors.toList());

        //save data to db
        terminalRepository.save(terminal);
        log.info("get terminal with id: {} and token: {}", terminalId, token);

        return ProfileDto.builder()
                .profileId(terminal.getProfile().getId())
                .terminalId(terminal.getId())
                .expiredAt(expiredAt)
                .token(token)
                .profileServices(profileServiceDtos)
                .build();
    }
}
