package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendor_services")
@Builder
public class VendorService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "service_category_id", referencedColumnName = "id")
    private ServiceCategory serviceCategory;

    @Column(unique = true)
    private String name;

    private Long price;
}
