package co.istad.spring_boot_2.controller;

import co.istad.spring_boot_2.dto.request.CreateCustomerRequest;
import co.istad.spring_boot_2.dto.response.CustomerResponse;
import co.istad.spring_boot_2.dto.request.UpdateCustomerRequest;
import co.istad.spring_boot_2.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService  customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody CreateCustomerRequest customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{phoneNumber}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse findByPhone(@PathVariable String phoneNumber) {
        return customerService.findByPhoneNumber(phoneNumber);
    }

    @PatchMapping("/{phone}")
    public CustomerResponse updateCustomer(@PathVariable String phone, @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        return customerService.updateByPhone(phone, updateCustomerRequest);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/disable-customer/{phone}")
    public void disableCustomer(@PathVariable String phone) {
        customerService.disableByCustomerPhone(phone);
    }

}
