package id.co.pcsindonesia.ia.ekyc.dto.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OcrCommandDto {
    @NotBlank
    private String photo;
}
