package id.co.pcsindonesia.ia.ekyc.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class VidaGlobalCommandDto<T> {
    private T parameters;
}
