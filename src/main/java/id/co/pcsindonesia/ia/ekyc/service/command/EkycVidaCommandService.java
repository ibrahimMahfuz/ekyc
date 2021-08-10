package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnCidCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.vidacid.VidaCidDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.VidaOcrDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.VidaStatusDto;

public interface EkycVidaCommandService extends EkycCommandService<VidaOcrDto, OcrCommandDto, VidaOcrDto, LnCidCommandDto, VidaCidDto>{
    @Override
    VidaOcrDto ocr(OcrCommandDto param) throws JsonProcessingException;
    public <T> VidaStatusDto<T> getStatus(String tid, Class<T> responseClass) throws InterruptedException;

}
