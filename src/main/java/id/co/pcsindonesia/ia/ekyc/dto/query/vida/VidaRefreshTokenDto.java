package id.co.pcsindonesia.ia.ekyc.dto.query.vida;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VidaRefreshTokenDto {
    private String accessToken;
}
