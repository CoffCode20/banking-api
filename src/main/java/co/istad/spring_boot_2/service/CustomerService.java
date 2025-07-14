package co.istad.spring_boot_2.service;

import co.istad.spring_boot_2.dto.request.CreateCustomerRequest;
import co.istad.spring_boot_2.dto.response.CustomerResponse;
import co.istad.spring_boot_2.dto.request.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest customer);

    List<CustomerResponse> findAll();

    CustomerResponse findByPhoneNumber(String phoneNumber);

    CustomerResponse updateByPhone(String phone, UpdateCustomerRequest updateCustomerRequest);

    void disableByCustomerPhone(String customerPhone);
}
