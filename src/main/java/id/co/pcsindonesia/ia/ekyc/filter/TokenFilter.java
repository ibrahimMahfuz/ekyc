package id.co.pcsindonesia.ia.ekyc.filter;

import id.co.pcsindonesia.ia.ekyc.entity.Terminal;
import id.co.pcsindonesia.ia.ekyc.repository.TerminalRepository;
import id.co.pcsindonesia.ia.ekyc.util.exception.DataNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@AllArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TerminalRepository terminalRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String tokenDirty = request.getHeader("Authorization");

        if (tokenDirty == null || tokenDirty.isEmpty() ){
            chain.doFilter(request, response);
            return;
        }

        String token = tokenDirty.replace("Bearer ", "");

        Terminal terminal = terminalRepository.findByToken(token).orElse(null);

        if (terminal == null) {
            handlerExceptionResolver.resolveException(request, response, null, new AccessDeniedException("your token is invalid"));
            return;
        }

        long now = LocalDate.now().toEpochSecond(LocalTime.now(), UTC );
        if (now > terminal.getExpired_at()){
            handlerExceptionResolver.resolveException(request, response, null, new AccessDeniedException("your token is expired"));
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                token,
                null,
                null
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
