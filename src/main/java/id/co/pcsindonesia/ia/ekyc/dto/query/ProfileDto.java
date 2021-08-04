package id.co.pcsindonesia.ia.ekyc.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {

    private UUID token;
    private Long expiredAt;
    private Long terminalId;
    private Long profileId;
    private List<ProfileServiceDto> profileServices;
}
