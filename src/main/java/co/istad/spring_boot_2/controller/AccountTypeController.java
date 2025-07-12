package co.istad.spring_boot_2.controller;


import co.istad.spring_boot_2.dto.request.CreateAccountTypeRequest;
import co.istad.spring_boot_2.dto.response.AccountRespond;
import co.istad.spring_boot_2.dto.response.AccountTypeResponse;
import co.istad.spring_boot_2.service.AccountTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountTypeResponse createAccountType(@Valid @RequestBody CreateAccountTypeRequest  createAccountTypeRequest) {

        return accountTypeService.createAccountType(createAccountTypeRequest);
    }
}
