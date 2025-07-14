package co.istad.spring_boot_2.controller;

import co.istad.spring_boot_2.dto.request.CustomerPhoneRequest;
import co.istad.spring_boot_2.dto.response.AccountRespond;
import co.istad.spring_boot_2.dto.request.CreateAccountRequest;
import co.istad.spring_boot_2.dto.request.UpdateAccountRequest;
import co.istad.spring_boot_2.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountRespond createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        return accountService.createAccount(createAccountRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountRespond> findAllAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/{actNo}")
    @ResponseStatus(HttpStatus.OK)
    public AccountRespond findAccountByActNo(@PathVariable String actNo) {
        return accountService.findAccountByActNo(actNo);
    }

    @GetMapping("/by-customer-phoneNumber")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountRespond> findAccountByCustomer(@Valid @RequestBody CustomerPhoneRequest customerNumber) {
        return accountService.findAccountByCustomerPhone(customerNumber);
    }

    @PatchMapping("/{actNo}")
    public AccountRespond updateAccount(@PathVariable String actNo, @RequestBody UpdateAccountRequest updateAccountRequest) {
        return accountService.updateAccount(actNo, updateAccountRequest);
    }

    @PutMapping("/disable/{actNo}")
    public ResponseEntity<?> disableAccountByActNo(@PathVariable String actNo) {
        accountService.disableAccountByActNo(actNo);
        return ResponseEntity.status(HttpStatus.OK).body("Your account disable successfully");
    }

    @DeleteMapping("/{actNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountByActNo(@PathVariable String actNo) {
        accountService.deleteAccountByActNo(actNo);
    }

}
