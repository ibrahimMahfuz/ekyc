package id.co.pcsindonesia.ia.ekyc.dto.query.vida;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VidaDemogDto {
    private String name;
    private Double score;
    private Double threshold;
    private Boolean match;
    private VidaExtraDto extra;
}
