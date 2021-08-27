package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DemogCommandDto {
    @NotBlank
    private Long nik;
    @NotBlank
    private String fullName;
    @NotBlank
    private LocalDate dob;
    @NotBlank
    private String phoneNo;
    @NotBlank
    private String email;
    @NotBlank
    private Double threshold;
}
