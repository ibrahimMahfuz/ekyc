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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Service
@AllArgsConstructor
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private TerminalRepository terminalRepository;
    private ProfileServiceRepository profileServiceRepository;

    @Override
    @Transactional
    public ProfileDto getProfileByTerminalId(Long terminalId) {
        Terminal terminal = terminalRepository.findById(terminalId).orElseThrow(DataNotFoundException::new);

        //set token
        UUID uuid =UUID.randomUUID();
        terminal.setToken(uuid);

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

        return ProfileDto.builder()
                .profileId(terminal.getProfile().getId())
                .terminalId(terminal.getId())
                .expiredAt(expiredAt)
                .token(uuid)
                .profileServices(profileServiceDtos)
                .build();
    }
}
