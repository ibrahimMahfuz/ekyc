package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VidaAuthCommandDto {
    @JsonAlias("client_id")
    private String clientId;
    @JsonAlias("client_secret")
    private String clientSecret;
    @JsonAlias("grant_type")
    private String grantType;
    @JsonAlias("scope")
    private String scope;

    @Override
    public String toString() {
        return "client_id="+clientId+"&"+"client_secret="+clientSecret+"&"+"grant_type="+grantType+"&"+"scope="+scope;
    }
}
