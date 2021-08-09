package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.repository.UserRepository;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    public User getOrCreate(User userParam){
        User user = userRepository.findById(userParam.getNik()).orElse(null);
        if (user == null){
            user = userRepository.save(userParam);
        }
        return user;
    }
}
