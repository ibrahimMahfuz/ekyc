package id.co.pcsindonesia.ia.ekyc.dto.query.asliri;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AsliRiExtraTaxDto {
    private String npwp;
    private String nik;
    private String matchResult;
    private String income;
    private String name;
    private String birthdate;
    private String birthplace;
    private String message;
}
