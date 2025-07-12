package co.istad.spring_boot_2.repository;

import co.istad.spring_boot_2.domain.AccountType;
import co.istad.spring_boot_2.dto.response.AccountTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
    Optional<AccountType> findAccountTypeByTypeName(String typeName);

    boolean existsAccountTypeByTypeName(String typeName);
}
