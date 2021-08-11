package id.co.pcsindonesia.ia.ekyc.dto.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCommandDto {
    private Long nik;
    private String name;
    private String pob;
    private LocalDate dob;
    private String phoneNumber;
    private String email;
}
