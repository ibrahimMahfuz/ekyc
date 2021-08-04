package id.co.pcsindonesia.ia.ekyc.repository;

import id.co.pcsindonesia.ia.ekyc.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
