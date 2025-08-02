package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    Optional<Media> findMediaByName(String name);

}
