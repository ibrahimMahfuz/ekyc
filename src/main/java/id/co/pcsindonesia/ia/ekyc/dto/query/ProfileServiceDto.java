package id.co.pcsindonesia.ia.ekyc.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileServiceDto {

    private Long vendorServiceId;
    private Long serviceCategoryId;
}
