package id.co.pcsindonesia.ia.ekyc.switcher;

import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileServiceDto;

import java.util.List;

public interface EkycSwitcher {
    public List<ProfileServiceDto> getService(String token);
    public Long ocrType(List<ProfileServiceDto> profileServiceDtoList);
    public Long livenessType(List<ProfileServiceDto> profileServiceDtoList);
    public Long demogType(List<ProfileServiceDto> profileServiceDtoList);
    public Long facematchType(List<ProfileServiceDto> profileServiceDtoList);
    public Long incomeType(List<ProfileServiceDto> profileServiceDtoList);
    public Long phoneType(List<ProfileServiceDto> profileServiceDtoList);
}
