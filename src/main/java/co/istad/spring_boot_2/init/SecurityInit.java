package co.istad.spring_boot_2.init;

import co.istad.spring_boot_2.domain.Role;
import co.istad.spring_boot_2.domain.User;
import co.istad.spring_boot_2.repository.RoleRepository;
import co.istad.spring_boot_2.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        if (roleRepository.count() == 0) {
            Role roleUSER = new Role();
            roleUSER.setName("USER");

            Role roleADMIN = new Role();
            roleADMIN.setName("ADMIN");

            Role roleSTAFF = new Role();
            roleSTAFF.setName("STAFF");

            Role roleCUSTOMER = new Role();
            roleCUSTOMER.setName("CUSTOMER");

            roleRepository.saveAll(List.of(roleUSER, roleADMIN, roleSTAFF, roleCUSTOMER));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(roleUSER, roleADMIN));
            admin.setDeleted(false);
            admin.setEnabled(true);

            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("staff"));
            staff.setRoles(Set.of(roleUSER, roleSTAFF));
            staff.setDeleted(false);
            staff.setEnabled(true);

            User customer = new User();
            customer.setUsername("customer");
            customer.setPassword(passwordEncoder.encode("customer"));
            customer.setRoles(Set.of(roleUSER, roleCUSTOMER));
            customer.setDeleted(false);
            customer.setEnabled(true);

            userRepository.saveAll(List.of(admin, staff, customer));

        }
    }
}
