package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LnFmCommandDto {
    @NotNull
    @Size(min = 16, max = 16)
    private Long nik;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNo;

    @NotBlank
    private String faceImage;
}
