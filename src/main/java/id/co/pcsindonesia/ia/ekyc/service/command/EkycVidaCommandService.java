package id.co.pcsindonesia.ia.ekyc.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.co.pcsindonesia.ia.ekyc.dto.command.LnFmCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.OcrCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.command.vida.VidaDemogCommandDto;
import id.co.pcsindonesia.ia.ekyc.dto.query.vida.*;

public interface EkycVidaCommandService extends
        EkycCommandService<
                VidaGlobalDto<VidaTransactionDto>,
                OcrCommandDto,
                VidaGlobalDto<VidaTransactionDto>,
                LnFmCommandDto,
                VidaGlobalDto<VidaTransactionDto>,
                VidaFmHandlerDto,
                VidaStatusDto<VidaFaceMatchDto>
                >{
    @Override
    VidaGlobalDto<VidaTransactionDto> ocr(OcrCommandDto param) throws JsonProcessingException;

    @Override
    VidaGlobalDto<VidaTransactionDto> liveness(LnFmCommandDto param) throws JsonProcessingException;

    @Override
    VidaGlobalDto<VidaTransactionDto> faceMatch(LnFmCommandDto param) throws JsonProcessingException;

    @Override
    VidaFmHandlerDto completeIdHandler(VidaStatusDto<VidaFaceMatchDto> param);

    public <T> VidaStatusDto<T> getStatus(String tid, Class<T> responseClass) throws InterruptedException;

    public VidaGlobalDto<VidaTransactionDto> demogLite(VidaDemogCommandDto vidaDemogCommandDto) throws JsonProcessingException;
}
