package id.co.pcsindonesia.ia.ekyc.service.auth.impl;

import id.co.pcsindonesia.ia.ekyc.entity.Auth;
import id.co.pcsindonesia.ia.ekyc.entity.Terminal;
import id.co.pcsindonesia.ia.ekyc.repository.TerminalRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final TerminalRepository terminalRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Terminal terminal = terminalRepository.findByToken(s).orElseThrow(RuntimeException::new);
        return new Auth(terminal.getId().toString(),
                null,
                null,
                true,
                true,
                true,
                true);
    }
}
