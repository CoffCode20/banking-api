package co.istad.spring_boot_2.service;

import co.istad.spring_boot_2.dto.request.CreateAccountTypeRequest;
import co.istad.spring_boot_2.dto.response.AccountTypeResponse;

public interface AccountTypeService {

    AccountTypeResponse createAccountType(CreateAccountTypeRequest request);

}
