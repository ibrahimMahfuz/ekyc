package id.co.pcsindonesia.ia.ekyc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

import java.util.List;

@Configuration
public class OAuth2Config {

    private static final String TOKEN_URL= "https://qa-sso.vida.id/auth/realms/vida/protocol/openid-connect/token";
    private static final String CLIENT_ID= "partner-pcs";
    private static final String CLIENT_SECRET= "87eecd71-efa6-4411-a1b4-948798ee3ccb";
    private static final String GRANT_TYPE= "client_credentials";
    private static final String SCOPE= "roles";

    @Bean
    protected OAuth2ProtectedResourceDetails oauth2Resource() {
        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setAccessTokenUri(TOKEN_URL);
        clientCredentialsResourceDetails.setClientId(CLIENT_ID);
        clientCredentialsResourceDetails.setClientSecret(CLIENT_SECRET);
        clientCredentialsResourceDetails.setGrantType(GRANT_TYPE);
        clientCredentialsResourceDetails.setScope(List.of(SCOPE));
        clientCredentialsResourceDetails.setAuthenticationScheme(AuthenticationScheme.form);
        return clientCredentialsResourceDetails;
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails = oauth2Resource();
        DefaultOAuth2ClientContext defaultOAuth2ClientContext = new DefaultOAuth2ClientContext(atr);
        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(oAuth2ProtectedResourceDetails, defaultOAuth2ClientContext);
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        oauth2RestTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
        return oauth2RestTemplate;
    }
}
