package id.co.pcsindonesia.ia.ekyc.dto.query;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileDto {

    private String token;
    private Long expiredAt;
    private Long terminalId;
    private Long profileId;
    private List<ProfileServiceDto> profileServices;
}
