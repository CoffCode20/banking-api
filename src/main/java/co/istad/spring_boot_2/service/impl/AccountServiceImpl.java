package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.Account;
import co.istad.spring_boot_2.domain.AccountType;
import co.istad.spring_boot_2.domain.Customer;
import co.istad.spring_boot_2.dto.request.CreateAccountRequest;
import co.istad.spring_boot_2.dto.request.CustomerPhoneRequest;
import co.istad.spring_boot_2.dto.request.UpdateAccountRequest;
import co.istad.spring_boot_2.dto.response.AccountRespond;
import co.istad.spring_boot_2.mapper.AccountMapper;
import co.istad.spring_boot_2.repository.AccountRepository;
import co.istad.spring_boot_2.repository.AccountTypeRepository;
import co.istad.spring_boot_2.repository.CustomerRepository;
import co.istad.spring_boot_2.service.AccountService;
import co.istad.spring_boot_2.utils.CurrencyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
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

        Account account = new Account();

        // Validate account type
        AccountType accountType = accountTypeRepository
                .findAccountTypeByTypeName(request.accountType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account Type Not Found"));

        // Validation customer phone number
        Customer customer = customerRepository
                .findByPhone(request.phone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer phone number not found"));

        // validation kyc
        if(customer.getKyc().getIsVerified().equals(false)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customer need to be verified");
        }

        switch (request.actCurrency()) {
            case CurrencyUtil.USD -> {
                if (request.balance().compareTo(BigDecimal.valueOf(10)) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance must be greater than 10 USD");
                }
                // Set over limit base on customer segment
                if (customer.getCustomerSegment().getSegmentName().equals("REGULAR")) {
                    account.setOverLimit(BigDecimal.valueOf(5000));
                } else if (customer.getCustomerSegment().getSegmentName().equals("SILVER")) {
                    account.setOverLimit(BigDecimal.valueOf(10000));
                } else {
                    account.setOverLimit(BigDecimal.valueOf(50000));
                }
            }
            case CurrencyUtil.KHR -> {
                if (request.balance().compareTo(BigDecimal.valueOf(40000)) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance must be greater than 40,000 KHR");
                }
                // Set over limit base on customer segment
                if (customer.getCustomerSegment().getSegmentName().equals("REGULAR")) {
                    account.setOverLimit(BigDecimal.valueOf(5000 * 4000));
                } else if (customer.getCustomerSegment().getSegmentName().equals("SILVER")) {
                    account.setOverLimit(BigDecimal.valueOf(10000 * 4000));
                } else {
                    account.setOverLimit(BigDecimal.valueOf(50000 * 4000));
                }
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Currency is not supported");
        }

        // Validate account no
        if (request.actNo() != null) {
            if (accountRepository.existsByActNo(request.actNo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Account with Act No %s already exists", request.actNo()));
            }
            account.setActNo(request.actNo());
        } else {
            String actNo;
            do {
                actNo = String.format("%09d", new Random().nextInt(1_000_000_000));
            } while (accountRepository.existsByActNo(actNo));
            account.setActNo(actNo);
        }

        // Set data logic
        account.setActName(request.actName());
        account.setActCurrency(request.actCurrency().name());
        account.setBalance(request.balance());
        account.setIsHide(false);
        account.setIsDeleted(false);
        account.setCustomer(customer);
        account.setAccountType(accountType);

        account = accountRepository.save(account);

        return accountMapper.accountToAccountResponse(account);
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
