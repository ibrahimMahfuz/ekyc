package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;
import id.co.pcsindonesia.ia.ekyc.entity.ServiceLog;
import id.co.pcsindonesia.ia.ekyc.repository.ServiceLogRepository;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycAsliRiCommandService;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EkycAsliRiCommandServiceImpl implements EkycAsliRiCommandService {

    private final RestTemplate restTemplate;
    private static final String EXTRA_TAX_URL = "https://api.asliri.id:8443/pcs_poc/verify_tax_extra";
    private static final String TOKEN = "OWIwNmFjOGYtYzZiOC00NGI1LTkwNzEtZWQ1OWVlZDk0YTRm";

    @Autowired
    public EkycAsliRiCommandServiceImpl(@Qualifier("asliriRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AsliRiGlobalDto<AsliRiExtraTaxDto> extraTaxVerification(AsliRiExtraTaxCommandDto asliRiExtraTaxCommandDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", TOKEN);

        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(asliRiExtraTaxCommandDto);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<AsliRiGlobalDto<AsliRiExtraTaxDto>> response = restTemplate.exchange(
                EXTRA_TAX_URL,
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(AsliRiGlobalDto.class, AsliRiExtraTaxDto.class).getType())
        );

        if (response.getStatusCode() == HttpStatus.OK){

            return response.getBody();
        }else {
            throw new RuntimeException("error get extraTaxVerification");
        }
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
