package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_logs")
@Builder
public class ServiceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "terminal_id", referencedColumnName = "id")
    private Terminal terminal;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "nik")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vendor_service_id", referencedColumnName = "id")
    private VendorService vendorService;

    private Boolean success;

    private String message;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date created_at;
}
