package id.co.pcsindonesia.ia.ekyc.repository;

import id.co.pcsindonesia.ia.ekyc.entity.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceLogRepository extends JpaRepository<ServiceLog, Long> {
}
