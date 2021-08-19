package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.vida.*;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.*;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@AllArgsConstructor
public class EkycVidaCommandServiceImpl implements EkycVidaCommandService {

    private final RestTemplate restTemplate;
    private static final String OCR_URL = "https://services-sandbox.vida.id/verify/v1/ktp/ocr";
    private static final String STATUS_URL = "https://services-sandbox.vida.id/verify/v1/transaction/";
    private static final String CID_URL = "https://services-sandbox.vida.id/verify/v1/face/match";
    private static final String DEMOG_URL = "https://services-sandbox.vida.id/verify/v1/demog-lite";
    private static final Double FACE_TRHESHOLD = 6.0;
    private static final Double CID_TRHESHOLD = 3.0;
    private static final Double DEMOG_TRHESHOLD = 1.0;


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
                OCR_URL,
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaGlobalDto.class, VidaTransactionDto.class).getType())
        );

         if (response.getStatusCode() == HttpStatus.OK){
             return response.getBody();
         }else {
             throw new RuntimeException("error get ocr");
         }
    }

    @Override
    public VidaGlobalDto<VidaTransactionDto> liveness(LnFmCommandDto param) throws JsonProcessingException {
        return null;
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
        VidaAnotherGlobalCommandDto<VidaFmCommandDto> body = new VidaAnotherGlobalCommandDto<>(CID_TRHESHOLD, vidaCidCommandDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<VidaGlobalDto<VidaTransactionDto>> response = restTemplate.exchange(
                CID_URL,
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaGlobalDto.class, VidaTransactionDto.class).getType())
        );

        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else {
            throw new RuntimeException("error get complete id");
        }
    }

    @Override
    public VidaFmHandlerDto completeIdHandler(VidaStatusDto<VidaFaceMatchDto> param) {
        return new VidaFmHandlerDto(param.getData().getResult().getMatch());
    }


    @Override
    public <T> VidaStatusDto<T> getStatus(String tid, Class<T> responseClass) throws InterruptedException {
        final String FULL_URL = STATUS_URL + tid;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<VidaStatusDto<T>> response =
        restTemplate.exchange(
                FULL_URL,
                HttpMethod.GET,
                entity,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
        );
        VidaStatusDto<T> body = response.getBody();
        if (Objects.equals(body.getData().getStatus(), "success")){
            return body;
        }else {
            Thread.sleep(1000);
            ResponseEntity<VidaStatusDto<T>> response2 = restTemplate.exchange(
                    FULL_URL,
                    HttpMethod.GET,
                    entity,
                    ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
            );
            VidaStatusDto<T> body2 = response2.getBody();
            if (Objects.equals(body2.getData().getStatus(), "success")){
                return body2;
            }else {
                Thread.sleep(2000);
                ResponseEntity<VidaStatusDto<T>> response3 =
                restTemplate.exchange(
                        FULL_URL,
                        HttpMethod.GET,
                        entity,
                        ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
                );
                VidaStatusDto<T> body3 = response3.getBody();
                if (Objects.equals(body3.getData().getStatus(), "success")) {
                    return body3;
                }else {
                    throw new RuntimeException("error get status ocr");
                }
            }
        }
    }

    @Override
    public VidaGlobalDto<VidaTransactionDto> demogLite(VidaDemogCommandDto vidaDemogCommandDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (vidaDemogCommandDto.getEmail() == null) vidaDemogCommandDto.setEmail("nullEmail@emai.com");
        if (vidaDemogCommandDto.getPhoneNo() == null ) vidaDemogCommandDto.setPhoneNo("081234567809");

        ObjectMapper objectMapper = new ObjectMapper();
        VidaAnotherGlobalCommandDto<VidaDemogCommandDto> body = new VidaAnotherGlobalCommandDto<>(DEMOG_TRHESHOLD, vidaDemogCommandDto);
        String bodyString = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<VidaGlobalDto<VidaTransactionDto>> response = restTemplate.exchange(
                DEMOG_URL,
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaGlobalDto.class, VidaTransactionDto.class).getType())
        );

        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else {
            throw new RuntimeException("error get demog");
        }
    }


}
