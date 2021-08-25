package id.co.pcsindonesia.ia.ekyc.util.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ekyc.vendor")
public class EkycVendorProperty {
    private Long vida;
    private Long vj;
    private Long asliRi;
}
