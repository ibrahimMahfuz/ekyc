package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EkycCommandService<T, P, U, Q, V, W, R> {
    public T ocr(P param) throws JsonProcessingException;
    public U liveness(Q param) throws JsonProcessingException;
    public V faceMatch(Q param) throws JsonProcessingException;
    public W completeIdHandler(R param);
}
