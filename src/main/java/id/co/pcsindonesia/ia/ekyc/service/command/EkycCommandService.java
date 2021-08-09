package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EkycCommandService<T, P, U, Q> {
    public T ocr(P param) throws JsonProcessingException;
    public U liveness(Q param);
}
