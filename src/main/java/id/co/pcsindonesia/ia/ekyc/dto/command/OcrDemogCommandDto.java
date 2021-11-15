package id.co.pcsindonesia.ia.ekyc.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcrDemogCommandDto {
    @NotBlank
    private String photo;
    @NotNull
    private Double threshold;

}
