package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.Customer;
import co.istad.spring_boot_2.dto.request.CreateCustomerRequest;
import co.istad.spring_boot_2.dto.response.CustomerResponse;
import co.istad.spring_boot_2.dto.request.UpdateCustomerRequest;
import co.istad.spring_boot_2.mapper.CustomerMapper;
import co.istad.spring_boot_2.repository.CustomerRepository;
import co.istad.spring_boot_2.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest customer) {

        if(customerRepository.existsByEmail(customer.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if(customerRepository.existsByPhone(customer.phone())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        }


        Customer createCustomer = customerMapper.customerRequestToCustomer(customer);
        createCustomer.setIsDeleted(false);

        log.info("Customer before creation: {}", createCustomer.getId());
        customerRepository.save(createCustomer);
        log.info("Customer after creation: {}", createCustomer.getId());

        return customerMapper.customerToCustomerResponse(createCustomer);
    }


    @Override
    public List<CustomerResponse> findAll() {
        List<Customer> customers = customerRepository.findAll();
        if(customers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        return customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .toList();

    }

    @Override
    public CustomerResponse findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhone(phoneNumber)
                .map(customerMapper::customerToCustomerResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number does not exist"));
    }


    @Override
    public CustomerResponse updateByPhone(String phone, UpdateCustomerRequest updateCustomerRequest) {

        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number does not exist"));
        customerMapper.toCustomerPartially(updateCustomerRequest, customer);
        Customer updated = customerRepository.save(customer);
        return customerMapper.customerToCustomerResponse(updated);
    }
}
