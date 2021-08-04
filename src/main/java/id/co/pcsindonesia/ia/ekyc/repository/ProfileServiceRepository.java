package id.co.pcsindonesia.ia.ekyc.repository;

import id.co.pcsindonesia.ia.ekyc.entity.Profile;
import id.co.pcsindonesia.ia.ekyc.entity.ProfileService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileServiceRepository extends JpaRepository<ProfileService, Long> {

    List<ProfileService> findAllByProfile(Profile profile);
}
