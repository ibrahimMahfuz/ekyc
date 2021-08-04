package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    private Date dob;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private Boolean verified;

}
