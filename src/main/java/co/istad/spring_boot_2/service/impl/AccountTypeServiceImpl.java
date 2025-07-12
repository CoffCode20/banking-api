package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.AccountType;
import co.istad.spring_boot_2.dto.request.CreateAccountTypeRequest;
import co.istad.spring_boot_2.dto.response.AccountTypeResponse;
import co.istad.spring_boot_2.mapper.AccountMapper;
import co.istad.spring_boot_2.repository.AccountTypeRepository;
import co.istad.spring_boot_2.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {
    private final AccountTypeRepository accountTypeRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountTypeResponse createAccountType(CreateAccountTypeRequest request) {
        String normalizedName = request.typeName().toUpperCase(Locale.ROOT);

        if(accountTypeRepository.existsAccountTypeByTypeName(normalizedName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account type already exists");
        }

        AccountType accountType = new AccountType();
        accountType.setTypeName(normalizedName);

        return accountMapper.toAccountTypeResponse(accountTypeRepository.save(accountType));
    }
}
