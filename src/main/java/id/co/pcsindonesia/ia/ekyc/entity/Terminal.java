package id.co.pcsindonesia.ia.ekyc.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "terminals")
@Builder
public class Terminal {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @Column(unique = true)
    private UUID token;

    private Long expired_at;
}
