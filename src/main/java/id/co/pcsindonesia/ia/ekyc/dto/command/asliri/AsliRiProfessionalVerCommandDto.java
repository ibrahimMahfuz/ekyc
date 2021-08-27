package id.co.pcsindonesia.ia.ekyc.dto.command.asliri;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AsliRiProfessionalVerCommandDto {
    private String trxId;
    private String nik;
    private String birthplace;
    private String name;
    private String birthdate;
    private String selfiePhoto;
}
