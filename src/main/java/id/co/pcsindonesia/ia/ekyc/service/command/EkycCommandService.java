package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EkycCommandService<T, P, U, Q, V> {
    public T ocr(P param) throws JsonProcessingException;
    public U liveness(Q param);
    public V completeId(Q param);
}
