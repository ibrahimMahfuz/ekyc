package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class User {

    @Id
    private Long nik;

    private String name;

    private String pob;

    private LocalDate dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    private Boolean verified;

}
