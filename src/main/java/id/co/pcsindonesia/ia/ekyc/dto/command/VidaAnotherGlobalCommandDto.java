package id.co.pcsindonesia.ia.ekyc.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class VidaAnotherGlobalCommandDto<T> {
    private Double threshold;
    private T parameters;
}
