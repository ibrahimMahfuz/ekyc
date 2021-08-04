package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile_services")
@Builder
public class ProfileService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_service_id", referencedColumnName = "id")
    private VendorService vendorService;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;
}
