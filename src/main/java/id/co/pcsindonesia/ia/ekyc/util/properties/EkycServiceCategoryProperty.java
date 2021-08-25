package id.co.pcsindonesia.ia.ekyc.util.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ekyc.service")
public class EkycServiceCategoryProperty {
    private Long ocr;
    private Long liveness;
    private Long faceMatch;
    private Long demog;
    private Long extraTax;

}
