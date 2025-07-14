package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.CustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerSegmentRepository extends JpaRepository<CustomerSegment, Integer> {
    boolean existsBySegmentName(String segmentName);
    Optional<CustomerSegment> findBySegmentNameIgnoreCase(String segmentName);

}
