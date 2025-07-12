package co.istad.spring_boot_2.mapper;

import co.istad.spring_boot_2.domain.Account;
import co.istad.spring_boot_2.domain.AccountType;
import co.istad.spring_boot_2.dto.response.AccountRespond;
import co.istad.spring_boot_2.dto.request.CreateAccountRequest;
import co.istad.spring_boot_2.dto.request.UpdateAccountRequest;
import co.istad.spring_boot_2.dto.response.AccountTypeResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "actNo", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "accountType", ignore = true) // ❗ We will set it manually later
    Account customerRequestToAccount(CreateAccountRequest createAccountRequest);

    @Mapping(target = "accountType", source = "account.accountType.typeName")
    AccountRespond accountToAccountResponse(Account account);

    AccountTypeResponse toAccountTypeResponse(AccountType accountType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toAccountPartially(UpdateAccountRequest updateAccountRequest, @MappingTarget Account account);
}
