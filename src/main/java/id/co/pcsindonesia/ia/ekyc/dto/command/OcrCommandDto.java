package id.co.pcsindonesia.ia.ekyc.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrCommandDto {
    @NotBlank
    private String photo;
}
