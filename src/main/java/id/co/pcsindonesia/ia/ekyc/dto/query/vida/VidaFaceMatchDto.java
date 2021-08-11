package id.co.pcsindonesia.ia.ekyc.dto.query.vida;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VidaFaceMatchDto {
    private String name;
    private Double score;
    private Double threshold;
    private Boolean match;
    private VidaExtraDto extra;

}
