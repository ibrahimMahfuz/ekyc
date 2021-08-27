package id.co.pcsindonesia.ia.ekyc.dto.query.asliri;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AsliRiProfessionalVerDto {
    private Boolean name;
    private Boolean birthdate;
    private Boolean birthplace;
    private String address;
    private Double selfiePhoto;
}
