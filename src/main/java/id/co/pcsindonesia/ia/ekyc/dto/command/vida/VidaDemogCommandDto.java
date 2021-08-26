package id.co.pcsindonesia.ia.ekyc.dto.command.vida;

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
public class VidaDemogCommandDto {
    private Long nik;
    private String fullName;
    private String dob;
    private String phoneNo;
    private String email;
}
