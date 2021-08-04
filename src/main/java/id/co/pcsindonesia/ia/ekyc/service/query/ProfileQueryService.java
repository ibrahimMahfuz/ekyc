package id.co.pcsindonesia.ia.ekyc.service.query;

import id.co.pcsindonesia.ia.ekyc.dto.query.ProfileDto;

public interface ProfileQueryService {

    public ProfileDto getProfileByTerminalId(Long terminalId);
}
