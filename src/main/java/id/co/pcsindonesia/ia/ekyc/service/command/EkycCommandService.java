package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EkycCommandService<T, P, U, Q, V, R, W, S> {
    public T ocr(P param) throws JsonProcessingException;
    public U liveness(Q param) throws JsonProcessingException;
    public V faceMatch(R param) throws JsonProcessingException;
    public W demog(S param) throws JsonProcessingException;
}
