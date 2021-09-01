package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.pcsindonesia.ia.ekyc.dto.command.ExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.PhoneCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiOcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiPhoneCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiProfessionalVerCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.*;
import id.co.pcsindonesia.ia.ekyc.service.command.EkycAsliRiCommandService;
import id.co.pcsindonesia.ia.ekyc.util.exception.VendorServiceUnavailableException;
import id.co.pcsindonesia.ia.ekyc.util.properties.AsliRiProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class EkycAsliRiCommandServiceImpl implements EkycAsliRiCommandService {

    private final RestTemplate restTemplate;
    private final AsliRiProperty asliRiProperty;

    @Autowired
    public EkycAsliRiCommandServiceImpl(@Qualifier("asliriRestTemplate") RestTemplate restTemplate, AsliRiProperty asliRiProperty) {
        this.restTemplate = restTemplate;
        this.asliRiProperty = asliRiProperty;
    }

    @Override
    public AsliRiGlobalDto<AsliRiExtraTaxDto> extraTaxVerification(ExtraTaxCommandDto extraTaxCommandDto) throws JsonProcessingException {
        log.info("access service asliri get extra tax");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", asliRiProperty.getToken());

        String trxId = UUID.randomUUID().toString().replace("-","x");

        AsliRiExtraTaxCommandDto asliRiExtraTaxCommandDto = AsliRiExtraTaxCommandDto
                .builder()
                .trxId(trxId)
                .nik(extraTaxCommandDto.getNik())
                .npwp(extraTaxCommandDto.getNpwp())
                .income(extraTaxCommandDto.getIncome())
                .name(extraTaxCommandDto.getName())
                .birthdate(extraTaxCommandDto.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .birthplace(extraTaxCommandDto.getBirthplace())
                .build();

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
    public AsliRiGlobalDto<AsliRiPhoneDto> phoneVerification(PhoneCommandDto phoneCommandDto) throws JsonProcessingException {
        log.info("access service asliri get phone");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", asliRiProperty.getToken());

        String trxId = UUID.randomUUID().toString().replace("-","x");
        String phone = phoneCommandDto.getPhoneNumber();

        if (phone.startsWith("0")){
            phone = "+62"+phone.substring(1);
        }else if (!phone.startsWith("+")){
            phone = "+62"+phone;
        }

        AsliRiPhoneCommandDto asliRiPhoneCommandDto = AsliRiPhoneCommandDto.builder()
                .trxId(trxId)
                .nik(String.valueOf(phoneCommandDto.getNik()))
                .phone(phone)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(asliRiPhoneCommandDto);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<AsliRiGlobalDto<AsliRiPhoneDto>> response = restTemplate.exchange(
                asliRiProperty.getPhoneUrl(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(AsliRiGlobalDto.class, AsliRiPhoneDto.class).getType())
        );
        return response.getBody();
    }

    @Override
    public AsliRiOcrDto ocr(OcrCommandDto param) throws JsonProcessingException {
        log.info("access service asliri get ocr");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", asliRiProperty.getToken());

        String trxId = UUID.randomUUID().toString().replace("-","x");
        String ktmImage = null;
        try{
            ktmImage = param.getPhoto().split(",")[1];
        }catch (Exception e){
            ktmImage = param.getPhoto();
        }
        AsliRiOcrCommandDto build = AsliRiOcrCommandDto.builder().trxId(trxId).ktpImage(ktmImage).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(build);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<AsliRiGlobalDto<AsliRiOcrDto>> response = restTemplate.exchange(
                asliRiProperty.getOcrUrl(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(AsliRiGlobalDto.class, AsliRiOcrDto.class).getType())
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }

    @Override
    public Object liveness(Object param) throws JsonProcessingException {
        throw new VendorServiceUnavailableException("service liveness not available in AsliRi service");
    }

    @Override
    public Boolean faceMatch(LnFmCommandDto param) throws JsonProcessingException {
        log.info("access service asliri get facematch");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", asliRiProperty.getToken());

        String trxId = UUID.randomUUID().toString().replace("-","x");
        String faceImage = null;
        try{
            faceImage = param.getFaceImage().split(",")[1];
        }catch (Exception e){
            faceImage = param.getFaceImage();
        }

        AsliRiProfessionalVerCommandDto build = AsliRiProfessionalVerCommandDto
                .builder()
                .trxId(trxId)
                .nik(String.valueOf(param.getNik()))
                .selfiePhoto(faceImage)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(build);

        HttpEntity<String> request = new HttpEntity<>(bodyString, headers);
        ResponseEntity<AsliRiGlobalDto<AsliRiProfessionalVerDto>> response = restTemplate.exchange(
                asliRiProperty.getFaceMatchUrl(),
                HttpMethod.POST,
                request,
                ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(AsliRiGlobalDto.class, AsliRiProfessionalVerDto.class).getType())
        );
        Double selfiePhoto = Objects.requireNonNull(response.getBody()).getData().getSelfiePhoto();
        Double threshold = (param.getThreshold() == null)? asliRiProperty.getFaceThreshold() : param.getThreshold();
        return selfiePhoto >= threshold;
    }


    @Override
    public Object demog(Object param) throws JsonProcessingException {
        throw new VendorServiceUnavailableException("service demog not available in AsliRi service");
    }
}
