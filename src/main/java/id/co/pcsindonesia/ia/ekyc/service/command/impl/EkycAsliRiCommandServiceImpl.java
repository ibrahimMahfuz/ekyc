package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycAsliRiCommandService;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServiceUnavailableException;
import id.co.pcsindonesia.ia.ekyc.util.properties.AsliRiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EkycAsliRiCommandServiceImpl implements EkycAsliRiCommandService {

    private final RestTemplate restTemplate;
    private final AsliRiProperty asliRiProperty;

    @Autowired
    public EkycAsliRiCommandServiceImpl(@Qualifier("asliriRestTemplate") RestTemplate restTemplate, AsliRiProperty asliRiProperty) {
        this.restTemplate = restTemplate;
        this.asliRiProperty = asliRiProperty;
    }

    @Override
    public AsliRiGlobalDto<AsliRiExtraTaxDto> extraTaxVerification(AsliRiExtraTaxCommandDto asliRiExtraTaxCommandDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", asliRiProperty.getToken());

        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(asliRiExtraTaxCommandDto);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<AsliRiGlobalDto<AsliRiExtraTaxDto>> response = restTemplate.exchange(
                asliRiProperty.getExtraTaxUrl(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(AsliRiGlobalDto.class, AsliRiExtraTaxDto.class).getType())
        );
        return response.getBody();
    }

    @Override
    public Object ocr(Object param) throws JsonProcessingException {
        throw new VendorServiceUnavailableException("service ocr not available in AsliRi service");
    }

    @Override
    public Object liveness(Object param) throws JsonProcessingException {
        throw new VendorServiceUnavailableException("service liveness not available in AsliRi service");
    }

    @Override
    public Object faceMatch(Object param) throws JsonProcessingException {
        throw new VendorServiceUnavailableException("service facematch not available in AsliRi service");
    }

    @Override
    public Object completeIdHandler(Object param) {
        throw new VendorServiceUnavailableException("service completeId handler not available in AsliRi service");
    }
}
