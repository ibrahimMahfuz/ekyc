package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCommandDto {
    @NotNull
    @Size(min = 16, max = 16)
    private Long nik;

    @NotBlank
    private String name;

    @NotBlank
    private String pob;

    @NotBlank
    @NotNull
    private LocalDate dob;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String email;
}
