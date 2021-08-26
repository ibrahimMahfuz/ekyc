package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.ExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.asliri.AsliRiExtraTaxCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiExtraTaxDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.asliri.AsliRiGlobalDto;

public interface EkycAsliRiCommandService extends EkycCommandService<Object, Object, Object, Object, Object, Object, Object>{

    public AsliRiGlobalDto<AsliRiExtraTaxDto> extraTaxVerification(ExtraTaxCommandDto extraTaxCommandDto) throws JsonProcessingException;
}
