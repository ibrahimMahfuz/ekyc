package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycSimulationCommandService;
import org.springframework.stereotype.Service;

@Service
public class EkycSimulationCommandServiceImpl implements EkycSimulationCommandService {
    @Override
    public Object ocr(Object param) throws JsonProcessingException {
        return null;
    }

    @Override
    public Object liveness(Object param) throws JsonProcessingException {
        return null;
    }

    @Override
    public Boolean faceMatch(Object param) throws JsonProcessingException {
        return true;
    }

    @Override
    public Boolean demog(Object param) throws JsonProcessingException {
        return true;
    }
}
