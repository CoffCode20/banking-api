package co.istad.spring_boot_2.service;

import co.istad.spring_boot_2.dto.request.CustomerPhoneRequest;
import co.istad.spring_boot_2.dto.response.AccountRespond;
import co.istad.spring_boot_2.dto.request.CreateAccountRequest;
import co.istad.spring_boot_2.dto.request.UpdateAccountRequest;

import java.util.List;

public interface AccountService {
    AccountRespond createAccount(CreateAccountRequest  request);

    List<AccountRespond> findAll();

    AccountRespond findAccountByActNo(String actNo);

    void disableAccountByActNo(String actNo);

    void deleteAccountByActNo(String actNo);

    AccountRespond updateAccount(String actNo, UpdateAccountRequest  request);

    List<AccountRespond> findAccountByCustomerPhone(CustomerPhoneRequest customerPhoneRequest);

}
