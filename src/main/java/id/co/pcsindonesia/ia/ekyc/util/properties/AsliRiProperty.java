package id.co.pcsindonesia.ia.ekyc.util.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "asliri.properties")
public class AsliRiProperty {
    private String extraTaxUrl;
    private String token;
}
