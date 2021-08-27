package id.co.pcsindonesia.ia.ekyc.service.query.impl;

import id.co.pcsindonesia.ia.ekyc.entity.User;
import id.co.pcsindonesia.ia.ekyc.repository.UserRepository;
import id.co.pcsindonesia.ia.ekyc.service.query.UserQueryService;
import id.co.pcsindonesia.ia.ekyc.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public Boolean isNikVerified(Long nik) {
        User user = userRepository.findById(nik).orElse(null);
        if (user == null) return false;
        return user.getVerified();
    }
}
