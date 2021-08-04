package id.co.pcsindonesia.ia.ekyc.repository;

import id.co.pcsindonesia.ia.ekyc.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
