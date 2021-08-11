package id.co.pcsindonesia.ia.ekyc.dto.query.vida;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VidaAuthDto {
    private String accessToken;
    private String expiresIn;
    private String refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String sessionState;
    private String scope;
}
