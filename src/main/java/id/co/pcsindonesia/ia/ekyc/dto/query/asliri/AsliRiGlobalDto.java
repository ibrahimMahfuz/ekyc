package id.co.pcsindonesia.ia.ekyc.dto.query.asliri;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AsliRiGlobalDto<T> {
    private Long timestamp;
    private Integer status;
    private T errors;
    private T data;
    private String trxId;
    private String refId;
}
