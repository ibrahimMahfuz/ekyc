package id.co.pcsindonesia.ia.ekyc.util.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "vida.properties")
public class VidaProperty {
    private String ocrUrl;
    private String livenessUrl;
    private String faceMatchUrl;
    private String demogLiteUrl;
    private String statusTransactionUrl;
    private String facematchStatusTransactionUrl;
    private Double faceThreshold;
    private Double demogThreshold;

}
