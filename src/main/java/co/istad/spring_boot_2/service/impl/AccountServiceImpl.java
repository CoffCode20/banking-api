package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.Account;
import co.istad.spring_boot_2.domain.AccountType;
import co.istad.spring_boot_2.domain.Customer;
import co.istad.spring_boot_2.dto.request.CustomerPhoneRequest;
import co.istad.spring_boot_2.dto.response.AccountRespond;
import co.istad.spring_boot_2.dto.request.CreateAccountRequest;
import co.istad.spring_boot_2.dto.request.UpdateAccountRequest;
import co.istad.spring_boot_2.mapper.AccountMapper;
import co.istad.spring_boot_2.repository.AccountRepository;
import co.istad.spring_boot_2.repository.AccountTypeRepository;
import co.istad.spring_boot_2.repository.CustomerRepository;
import co.istad.spring_boot_2.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountRespond createAccount(CreateAccountRequest request) {

        Customer customer = customerRepository.findByPhone(request.phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        // validation account type
        AccountType accountType = accountTypeRepository.findAccountTypeByTypeName(request.accountType().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account type not found"));

        String typeName = request.accountType().toUpperCase(Locale.ROOT);
        String phoneNumber = request.phoneNumber();

        if (accountRepository.existsByCustomer_PhoneAndAccountType_TypeName(phoneNumber, typeName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer already has an account of this type");
        }

        Account account = accountMapper.customerRequestToAccount(request);
        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setOverLimit(BigDecimal.valueOf(10000));
        account.setIsDeleted(false);

        return accountMapper.accountToAccountResponse(accountRepository.save(account));
    }

    @Override
    public List<AccountRespond> findAll() {
        List<AccountRespond> accounts = accountRepository.findAll().stream()
                .filter(account -> account.getIsDeleted().equals(false))
                .map(accountMapper::accountToAccountResponse)
                .collect(Collectors.toList());
        // validation if list of account empty
        if (accounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        return accounts;
    }

    @Override
    public AccountRespond findAccountByActNo(String actNo) {

        return accountRepository.findAccountByActNo(actNo)
                .filter(account -> account.getIsDeleted().equals(false))
                .map(accountMapper::accountToAccountResponse)
                .orElseThrow(()  -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Override
    public void disableAccountByActNo(String actNo) {
        Account accountToDelete = accountRepository.findAccountByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        accountToDelete.setIsDeleted(true);

        accountRepository.save(accountToDelete);
    }

    @Override
    public void deleteAccountByActNo(String actNo) {
        Account deleteAccount = accountRepository.findAccountByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        accountRepository.delete(deleteAccount);

    }

    @Override
    public AccountRespond updateAccount(String actNo, UpdateAccountRequest request) {

        Account accountToUpdate = accountRepository.findAccountByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        accountMapper.toAccountPartially(request,accountToUpdate);

        return accountMapper.accountToAccountResponse(accountRepository.save(accountToUpdate));
    }

    @Override
    public List<AccountRespond> findAccountByCustomerPhone(CustomerPhoneRequest customerPhoneRequest) {

        // validation Customer
        if (!customerRepository.existsByPhone(customerPhoneRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        List<Account> accounts = accountRepository.findAccountByCustomer_Phone(customerPhoneRequest.phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return accounts.stream()
                .filter(account -> account.getIsDeleted().equals(false))
                .map(accountMapper::accountToAccountResponse)
                .collect(Collectors.toList());
    }
}
