package id.co.pcsindonesia.ia.ekyc.util.exception;

import id.co.pcsindonesia.ia.ekyc.dto.query.ErrorWithObectDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.GlobalErrorDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.VidaGlobalErrorDto;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<GlobalErrorDto> globalHandler(Exception ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RequestTimeOutException.class})
    public ResponseEntity<GlobalErrorDto> globalHandler(RequestTimeOutException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.REQUEST_TIMEOUT.value(), ex.getMessage(), HttpStatus.REQUEST_TIMEOUT.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(value = {OAuth2Exception.class})
    public ResponseEntity<ErrorWithObectDto<VidaGlobalErrorDto>> oauth2Exception(OAuth2Exception ex) {
        VidaGlobalErrorDto vidaGlobalErrorDto = new VidaGlobalErrorDto();
        String summary = null;
        try{
           summary = ex.getSummary().split("errors=")[1];
        }catch (Exception $e){
            summary = ex.getSummary();
        }

        vidaGlobalErrorDto.setErrorMessageFromVida(summary);
        ErrorWithObectDto<VidaGlobalErrorDto> vidaGlobalErrorDtoErrorWithObectDto = new ErrorWithObectDto<>(
                ex.getHttpErrorCode(),
                ex.getMessage(),
                ex.getOAuth2ErrorCode(),
                vidaGlobalErrorDto
        );
        return new ResponseEntity<>(vidaGlobalErrorDtoErrorWithObectDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<GlobalErrorDto> forbiddenHandler(AccessDeniedException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.FORBIDDEN.value(), ex.getMessage(), HttpStatus.FORBIDDEN.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<GlobalErrorDto> dataNotFoundHandler(DataNotFoundException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidDataAccessApiUsageException.class})
    public ResponseEntity<GlobalErrorDto> dataNotFoundHandler(InvalidDataAccessApiUsageException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {VendorServiceUnavailableException.class})
    public ResponseEntity<GlobalErrorDto> vendorServiceUnavailable(VendorServiceUnavailableException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = {VendorServerException.class})
    public ResponseEntity<GlobalErrorDto> vendorServer(VendorServerException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {VendorClienException.class})
    public ResponseEntity<GlobalErrorDto> vendorClient(VendorClienException ex) {
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<GlobalErrorDto> unauthenticatedHandler(AuthenticationException ex) {
        if (ex.getMessage().equals("No value present")) ex = new InternalAuthenticationServiceException("username or password is wrong");
        GlobalErrorDto GlobalErrorResponse = new GlobalErrorDto(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(GlobalErrorResponse, HttpStatus.UNAUTHORIZED);
    }

}
