package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.KYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KYCRepository extends JpaRepository<KYC, String> {

    boolean existsByNationalCodeId(String nationalCodeId);

    Optional<KYC> findKYCByNationalCodeId(String nationalCodeId);
}
