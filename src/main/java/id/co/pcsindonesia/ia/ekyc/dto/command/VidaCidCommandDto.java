package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VidaCidCommandDto {
            private Long nik;
            private String fullName;
            private String dob;
            private String email;
            private String phoneNo;
            private Double faceThresold;
            private String faceImage;
}
