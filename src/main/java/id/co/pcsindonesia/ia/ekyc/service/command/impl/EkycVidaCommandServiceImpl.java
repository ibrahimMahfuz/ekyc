package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.vida.*;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.*;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import id.co.pcsindonesia.ia.ekyc.util.exception.RequestTimeOutException;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServerException;
import id.co.pcsindonesia.ia.ekyc.util.properties.VidaProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class EkycVidaCommandServiceImpl implements EkycVidaCommandService {

    private final RestTemplate restTemplate;
    private final VidaProperty vidaProperty;

    @Autowired
    public EkycVidaCommandServiceImpl(@Qualifier("vidaRestTemplate") RestTemplate restTemplate, VidaProperty vidaProperty) {
        this.restTemplate = restTemplate;
        this.vidaProperty = vidaProperty;
    }


    @Override
    public VidaGlobalDto<VidaTransactionDto> ocr(OcrCommandDto param) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        VidaOcrCommandDto vidaOcrCommandDto = new VidaOcrCommandDto(param.getPhoto());
        VidaGlobalCommandDto<VidaOcrCommandDto> body = new VidaGlobalCommandDto<>(vidaOcrCommandDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<VidaGlobalDto<VidaTransactionDto>> response = restTemplate.exchange(
                vidaProperty.getOcrUlr(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaGlobalDto.class, VidaTransactionDto.class).getType())
        );
        return response.getBody();
    }

    @Override
    public VidaGlobalDto<VidaTransactionDto> liveness(LnFmCommandDto param) throws JsonProcessingException {
        throw new VendorServerException("vida liveness service not found");
    }

    @Override
    public VidaGlobalDto<VidaTransactionDto> faceMatch(LnFmCommandDto param) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        VidaFmCommandDto vidaCidCommandDto = VidaFmCommandDto.builder()
                .nik(param.getNik())
                .phoneNo(param.getPhoneNo())
                .email(param.getEmail())
                .faceImage(param.getFaceImage())
                .build();
        VidaAnotherGlobalCommandDto<VidaFmCommandDto> body = new VidaAnotherGlobalCommandDto<>(vidaProperty.getFaceThreshold(), vidaCidCommandDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<VidaGlobalDto<VidaTransactionDto>> response = restTemplate.exchange(
                vidaProperty.getFaceMatchUrl(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaGlobalDto.class, VidaTransactionDto.class).getType())
        );
        return response.getBody();
    }

    @Override
    public VidaFmHandlerDto completeIdHandler(VidaStatusDto<VidaFaceMatchDto> param) {
        return new VidaFmHandlerDto(param.getData().getResult().getMatch());
    }


    @Override
    public <T> VidaStatusDto<T> getStatus(String tid, Class<T> responseClass) throws InterruptedException {
        final String FULL_URL = vidaProperty.getStatusTransactionUrl() + tid;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return sendGetStatus(FULL_URL, entity, responseClass, 1);
    }

    private <K> VidaStatusDto<K> sendGetStatus(String url, HttpEntity<String> entity, Class<K> responseClass, Integer loop) throws InterruptedException {
        Thread.sleep((loop*1000));
        ResponseEntity<VidaStatusDto<K>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
                );
        VidaStatusDto<K> body = response.getBody();
        if (Objects.equals(body.getData().getStatus(), "success")){
            return body;
        } else if (loop == 3){
           throw new RequestTimeOutException("get status too long");
        } else {
            return sendGetStatus(url, entity, responseClass, loop+1);
        }
    }

    @Override
    public VidaGlobalDto<VidaTransactionDto> demogLite(VidaDemogCommandDto vidaDemogCommandDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (vidaDemogCommandDto.getEmail() == null) vidaDemogCommandDto.setEmail("nullEmail@emai.com");
        if (vidaDemogCommandDto.getPhoneNo() == null ) vidaDemogCommandDto.setPhoneNo("081234567809");

        ObjectMapper objectMapper = new ObjectMapper();
        VidaAnotherGlobalCommandDto<VidaDemogCommandDto> body = new VidaAnotherGlobalCommandDto<>(vidaProperty.getDemogThreshold(), vidaDemogCommandDto);
        String bodyString = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<VidaGlobalDto<VidaTransactionDto>> response = restTemplate.exchange(
                vidaProperty.getDemogLiteUrl(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaGlobalDto.class, VidaTransactionDto.class).getType())
        );
        return response.getBody();
    }


}
