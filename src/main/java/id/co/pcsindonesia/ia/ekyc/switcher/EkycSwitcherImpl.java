package id.co.pcsindonesia.ia.ekyc.switcher;

import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileServiceDto;
import id.co.pcsindonesia.ia.ekyc.entity.ProfileService;
import id.co.pcsindonesia.ia.ekyc.entity.Terminal;
import id.co.pcsindonesia.ia.ekyc.repository.ProfileServiceRepository;
import id.co.pcsindonesia.ia.ekyc.repository.TerminalRepository;
import id.co.pcsindonesia.ia.ekyc.repository.VendorServiceRepository;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServiceUnavailableException;
import id.co.pcsindonesia.ia.ekyc.util.properties.EkycServiceCategoryProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EkycSwitcherImpl implements EkycSwitcher{

    private final ProfileServiceRepository profileServiceRepository;
    private final TerminalRepository terminalRepository;
    private final VendorServiceRepository vendorServiceRepository;
    private final EkycServiceCategoryProperty ekycServiceCategoryProperty;

    @Override
    public List<ProfileServiceDto> getService(String token) {
        Terminal terminal = terminalRepository.findByToken(token).orElseThrow();
        List<ProfileService> allByProfile = profileServiceRepository.findAllByProfile(terminal.getProfile());
        return allByProfile.stream()
                .map(profileService -> new ProfileServiceDto(profileService.getVendorService().getId(), profileService.getVendorService().getServiceCategory().getId())).collect(Collectors.toList());
    }

    @Override
    public Long getOcrType(List<ProfileServiceDto> profileServiceDtoList) {
        ProfileServiceDto service_not_registerd = profileServiceDtoList
                .stream()
                .filter(profileServiceDto -> profileServiceDto.getServiceCategoryId().equals(ekycServiceCategoryProperty.getOcr()))
                .findFirst()
                .orElseThrow(() -> new VendorServiceUnavailableException("we could not find ocr service in your profile, please check your terminal profile"));
        return vendorServiceRepository.findById(service_not_registerd.getVendorServiceId()).orElseThrow().getVendor().getId();
    }
}
