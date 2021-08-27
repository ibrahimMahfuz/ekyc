package id.co.pcsindonesia.ia.ekyc.util.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "asliri.properties")
public class AsliRiProperty {
    private String ocrUrl;
    private String livenessUrl;
    private String faceMatchUrl;
    private String demogUrl;
    private String extraTaxUrl;
    private Double faceThreshold;
    private String token;
}
