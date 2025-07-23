package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
