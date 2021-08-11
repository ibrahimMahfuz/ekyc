package id.co.pcsindonesia.ia.ekyc.service.command;

import id.co.pcsindonesia.ia.ekyc.dto.query.GetOrCreateUserDto;
import id.co.pcsindonesia.ia.ekyc.entity.User;

public interface UserCommandService {
    public GetOrCreateUserDto getOrCreate(User userParam);

    void updateUser(User orCreate);
}
