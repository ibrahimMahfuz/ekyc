package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LnFmCommandDto {
    @NotNull
    @Size(min = 16, max = 16)
    private Long nik;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNo;

    @NotBlank
    private String faceImage;

    @NotBlank
    private Double threshold;
}
