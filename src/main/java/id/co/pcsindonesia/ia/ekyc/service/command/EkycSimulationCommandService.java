package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EkycSimulationCommandService extends EkycCommandService<Object, Object, Object, Object, Boolean, Object, Boolean, Object>{

    @Override
    Object ocr(Object param) throws JsonProcessingException;

    @Override
    Object liveness(Object param) throws JsonProcessingException;

    @Override
    Boolean faceMatch(Object param) throws JsonProcessingException;

    @Override
    Boolean demog(Object param) throws JsonProcessingException;
}
