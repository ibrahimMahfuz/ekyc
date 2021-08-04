package id.co.pcsindonesia.ia.ekyc.dto.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GlobalErrorDto extends BaseDto {

    private String error;

    public GlobalErrorDto(Integer code, String message, String error) {
        super(code, message);
        this.error = error;
    }
}
