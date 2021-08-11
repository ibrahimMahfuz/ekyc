package id.co.pcsindonesia.ia.ekyc.dto.query;

import id.co.pcsindonesia.ia.ekyc.entity.User;
import lombok.Data;

@Data
public class GetOrCreateUserDto {
    private Boolean isGet;
    private User user;
}
