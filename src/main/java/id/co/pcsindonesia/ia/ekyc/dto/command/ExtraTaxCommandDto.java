package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExtraTaxCommandDto {
    @NotNull
    @NotBlank
    private String nik;
    @NotNull
    @NotBlank
    private String npwp;
    @NotNull
    @NotBlank
    private String income;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private LocalDate birthdate;
    @NotNull
    @NotBlank
    private String birthplace;
}
