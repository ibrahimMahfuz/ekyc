package id.co.pcsindonesia.ia.ekyc.service.command;

import id.co.pcsindonesia.ia.ekyc.entity.User;

public interface UserCommandService {
    public User getOrCreate(User userParam);
}
