package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Customer> findByPhoneAndIsDeletedIsFalse(String number);

    Optional<Customer> findByPhone(String number);

    List<Customer> findAllByIsDeletedIsFalse();

    @Modifying
    @Query("""
        UPDATE Customer c SET c.isDeleted = TRUE
            WHERE c.phone = ?1
    """)
    void disableByCustomerPhone(String customerPhone);
}
