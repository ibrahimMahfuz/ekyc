package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.VidaGlobalCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.VidaOcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.VidaOcrDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.VidaStatusDto;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycVidaCommandService;
import id.co.pcsindonesia.ia.ekyc.service.command.UserCommandService;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class EkycVidaCommandServiceImpl<U,Q> implements EkycVidaCommandService<U,Q> {

    private final OAuth2RestTemplate oAuth2RestTemplate;
    private static final String OCR_URL = "https://demo-verify.vida.id/1.3/ktp-ocr";
    private static final String STATUS_URL = "https://demo-verify.vida.id/1.3/transaction/";

    @Override
    public VidaOcrDto ocr(OcrCommandDto param) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(oAuth2RestTemplate.getAccessToken().getValue());

        VidaOcrCommandDto vidaOcrCommandDto = new VidaOcrCommandDto(param.getPhoto());
        VidaGlobalCommandDto<VidaOcrCommandDto> body = new VidaGlobalCommandDto<>(vidaOcrCommandDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<VidaOcrDto> vidaOcrDtoResponseEntity = oAuth2RestTemplate.postForEntity(OCR_URL, request, VidaOcrDto.class);

         if (vidaOcrDtoResponseEntity.getStatusCode() == HttpStatus.OK){
             return vidaOcrDtoResponseEntity.getBody();
         }else {
             throw new RuntimeException("error get ocr");
         }
    }

    @Override
    public U liveness(Q param) {
        return null;
    }

    @Override
    public <T> VidaStatusDto<T> getStatus(String tid, Class<T> responseClass) throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(oAuth2RestTemplate.getAccessToken().getValue());

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<VidaStatusDto<T>> response = oAuth2RestTemplate.exchange(
                STATUS_URL + tid,
                HttpMethod.GET,
                entity,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
        );
        VidaStatusDto<T> body = response.getBody();
        if (Objects.equals(body.getStatus(), "success")){
            return body;
        }else {
            Thread.sleep(1000);
            ResponseEntity<VidaStatusDto<T>> response2 = oAuth2RestTemplate.exchange(
                    STATUS_URL + tid,
                    HttpMethod.GET,
                    entity,
                    ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
            );
            VidaStatusDto<T> body2 = response2.getBody();
            if (Objects.equals(body2.getStatus(), "success")){
                return body2;
            }else {
                Thread.sleep(2000);
                ResponseEntity<VidaStatusDto<T>> response3 = oAuth2RestTemplate.exchange(
                        STATUS_URL + tid,
                        HttpMethod.GET,
                        entity,
                        ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(VidaStatusDto.class, responseClass).getType())
                );
                VidaStatusDto<T> body3 = response3.getBody();
                if (Objects.equals(body3.getStatus(), "success")) {
                    return body3;
                }else {
                    throw new RuntimeException("error get status ocr");
                }
            }
        }
    }

}
