package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.KYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KYCRepository extends JpaRepository<KYC, String> {

}
