package id.co.pcsindonesia.ia.ekyc.util.exception;

import id.co.pcsindonesia.ia.ekyc.dto.query.GlobalErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<GlobalErrorDto> globalHandler(Exception ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<GlobalErrorDto> forbiddenHandler(AccessDeniedException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.FORBIDDEN.value(), ex.getMessage(), HttpStatus.FORBIDDEN.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<GlobalErrorDto> unauthenticatedHandler(AuthenticationException ex) {
        if (ex.getMessage().equals("No value present")) ex = new InternalAuthenticationServiceException("username or password is wrong");
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.UNAUTHORIZED);
    }

}
