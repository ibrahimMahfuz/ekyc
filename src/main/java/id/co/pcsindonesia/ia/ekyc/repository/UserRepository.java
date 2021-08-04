package id.co.pcsindonesia.ia.ekyc.repository;

import id.co.pcsindonesia.ia.ekyc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
