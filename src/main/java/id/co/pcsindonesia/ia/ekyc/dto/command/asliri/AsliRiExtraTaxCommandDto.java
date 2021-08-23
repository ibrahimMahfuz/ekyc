package id.co.pcsindonesia.ia.ekyc.dto.command.asliri;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AsliRiExtraTaxCommandDto {
    private String trxId;
    private String nik;
    private String npwp;
    private String income;
    private String name;
    private String birthdate;
    private String birthplace;
}
