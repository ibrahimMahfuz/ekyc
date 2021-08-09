package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.VidaOcrDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.VidaStatusDto;

public interface EkycVidaCommandService<U, Q> extends EkycCommandService<VidaOcrDto, OcrCommandDto, U, Q>{
    @Override
    VidaOcrDto ocr(OcrCommandDto param) throws JsonProcessingException;

    public <T> VidaStatusDto<T> getStatus(String tid, Class<T> responseClass) throws InterruptedException;
}
