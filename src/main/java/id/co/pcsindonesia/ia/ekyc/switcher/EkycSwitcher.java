package id.co.pcsindonesia.ia.ekyc.switcher;

import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileServiceDto;

import java.util.List;

public interface EkycSwitcher {
    public List<ProfileServiceDto> getService(String token);
    public Long getOcrType(List<ProfileServiceDto> profileServiceDtoList);
}
