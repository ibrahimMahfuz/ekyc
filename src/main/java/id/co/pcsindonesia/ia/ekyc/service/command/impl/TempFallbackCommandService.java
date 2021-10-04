package id.co.pcsindonesia.ia.ekyc.service.command.impl;

import id.co.pcsindonesia.ia.ekyc.entity.TempFallback;
import id.co.pcsindonesia.ia.ekyc.repository.TempFallbackRepository;
import id.co.pcsindonesia.ia.ekyc.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TempFallbackCommandService {

    private final TempFallbackRepository tempFallbackRepository;

    public Boolean checkIfVida(){
        TempFallback fallback = tempFallbackRepository.findById(1L).orElseThrow(() -> new DataNotFoundException("fallback data not found"));
        return !fallback.getDate().isEqual(LocalDate.now()) || fallback.getCount() < 70;
    }

    @Transactional
    public void addRequestToday(){
        TempFallback fallback = tempFallbackRepository.findById(1L).orElseThrow(() -> new DataNotFoundException("fallback data not found"));
        if (fallback.getDate().isEqual(LocalDate.now())){
            var count = fallback.getCount()+1;
            fallback.setCount(count);
        }else {
            fallback.setCount(1);
            fallback.setDate(LocalDate.now());
        }
        tempFallbackRepository.save(fallback);
    }

}
