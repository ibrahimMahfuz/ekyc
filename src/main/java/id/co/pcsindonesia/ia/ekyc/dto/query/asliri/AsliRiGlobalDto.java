package id.co.pcsindonesia.ia.ekyc.dto.query.asliri;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import id.co.pcsindonesia.ia.ekyc.util.jackson.ToMapStringStringJsonDeserializer;
import id.co.pcsindonesia.ia.ekyc.util.jackson.ToStringJsonDeserializer;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AsliRiGlobalDto<T> {
    private Long timestamp;
    private Integer status;
    @JsonDeserialize(using = ToMapStringStringJsonDeserializer.class)
    private Map<String, String> errors;
    private T data;
    private String trxId;
    private String refId;
}
