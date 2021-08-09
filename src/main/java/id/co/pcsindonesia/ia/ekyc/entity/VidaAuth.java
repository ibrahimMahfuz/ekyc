package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vida_auths")
@Builder
public class VidaAuth {

    @Id
    private Long id;

    @Lob
    @Column(name = "access_token", length = 1024)
    private String accessToken;
    @Column(name = "expires_in")
    private String expiresIn;
    @Column(name = "refresh_expires_in")
    private String refreshExpiresIn;
    @Lob
    @Column(name = "refresh_token", length = 1024)
    private String refreshToken;
    @Column(name = "token_type")
    private String tokenType;
    @Column(name = "session_state")
    private String sessionState;
    private String scope;

}
