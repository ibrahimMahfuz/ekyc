package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycSimulationCommandService;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public AsliRiGlobalDto<AsliRiExtraTaxDto> extraTaxVerification() throws JsonProcessingException {
        AsliRiExtraTaxDto asliRiExtraTaxDto = new AsliRiExtraTaxDto();
        asliRiExtraTaxDto.setNpwp("true");
        asliRiExtraTaxDto.setNik("true");
        asliRiExtraTaxDto.setIncome("AMIDST");
        asliRiExtraTaxDto.setMatchResult("true");
        asliRiExtraTaxDto.setBirthdate("true");
        asliRiExtraTaxDto.setBirthplace("true");
        asliRiExtraTaxDto.setName("true");

        AsliRiGlobalDto<AsliRiExtraTaxDto> data = new AsliRiGlobalDto<>();
        data.setData(asliRiExtraTaxDto);
        data.setStatus(200);
        data.setTimestamp(16303060000L);
        data.setTrxId(UUID.randomUUID().toString().replace('-','x'));
        data.setRefId(UUID.randomUUID().toString());

        return data;
    }
}
