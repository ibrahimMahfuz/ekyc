package id.co.pcsindonesia.ia.ekyc.dto.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorWithObectDto<T> extends GlobalErrorDto{

    private T errors;

    public ErrorWithObectDto(Integer code, String message, String error, T errors) {
        super(code, message, error);
        this.errors = errors;
    }
}
