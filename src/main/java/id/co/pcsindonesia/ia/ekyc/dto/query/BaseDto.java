package id.co.pcsindonesia.ia.ekyc.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseDto {

    protected Integer code;
    protected String message;
}
