package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.ExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.PhoneCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiOcrDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiPhoneDto;

public interface EkycAsliRiCommandService extends EkycCommandService<AsliRiOcrDto, OcrCommandDto, Object, Object, Boolean, LnFmCommandDto, Object, Object>{
    public AsliRiGlobalDto<AsliRiExtraTaxDto> extraTaxVerification(ExtraTaxCommandDto extraTaxCommandDto) throws JsonProcessingException;
    public AsliRiGlobalDto<AsliRiPhoneDto> phoneVerification(PhoneCommandDto phoneCommandDto) throws JsonProcessingException;
}
