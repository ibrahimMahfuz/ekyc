package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import id.co.pcsindonesia.ia.ekyc.dto.query.GetOrCreateUserDto;
import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.repository.UserRepository;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    public GetOrCreateUserDto getOrCreate(User userParam){
        User user = userRepository.findById(userParam.getNik()).orElse(null);
        GetOrCreateUserDto getOrCreateUserDto = new GetOrCreateUserDto();
        getOrCreateUserDto.setIsGet(true);
        if (user == null){
            user = userRepository.save(userParam);
            getOrCreateUserDto.setIsGet(false);
        }
        getOrCreateUserDto.setUser(user);
        return getOrCreateUserDto;
    }

    @Override
    public void updateUser(User orCreate) {
        userRepository.save(orCreate);
    }
}
