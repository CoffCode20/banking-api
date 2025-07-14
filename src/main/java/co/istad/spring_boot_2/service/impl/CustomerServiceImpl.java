package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.Customer;
import co.istad.spring_boot_2.domain.CustomerSegment;
import co.istad.spring_boot_2.domain.KYC;
import co.istad.spring_boot_2.dto.request.CreateCustomerRequest;
import co.istad.spring_boot_2.dto.response.CustomerResponse;
import co.istad.spring_boot_2.dto.request.UpdateCustomerRequest;
import co.istad.spring_boot_2.mapper.CustomerMapper;
import co.istad.spring_boot_2.repository.CustomerRepository;
import co.istad.spring_boot_2.repository.CustomerSegmentRepository;
import co.istad.spring_boot_2.repository.KYCRepository;
import co.istad.spring_boot_2.service.CustomerService;
import jakarta.transaction.Transactional;
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
    private final CustomerSegmentRepository customerSegmentRepository;
    private final KYCRepository kycRepository;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest customer) {

        if(customerRepository.existsByEmail(customer.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if(customerRepository.existsByPhone(customer.phone())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        }

        CustomerSegment segment = customerSegmentRepository.findBySegmentNameIgnoreCase(customer.segment().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Segment not found"));

        Customer createCustomer = customerMapper.customerRequestToCustomer(customer);
        createCustomer.setCustomerSegment(segment);
        createCustomer.setIsDeleted(false);

        if (!kycRepository.existsByNationalCodeId(customer.nationalCardId())){
            // auto set kyc for customer
            KYC kyc = new KYC();
            kyc.setNationalCodeId(customer.nationalCardId());
            kyc.setIsVerified(false);
            kyc.setIsDeleted(false);
            kyc.setCustomer(createCustomer);

            customerRepository.save(createCustomer);
            kycRepository.save(kyc);
            log.info("Customer before creation: {}", createCustomer.getId());
            customerRepository.save(createCustomer);
            log.info("Customer after creation: {}", createCustomer.getId());
            return customerMapper.customerToCustomerResponse(createCustomer);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "National card already exists");

    }


    @Override
    public List<CustomerResponse> findAll() {
        List<Customer> customers = customerRepository.findAllByIsDeletedIsFalse();
        if(customers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        return customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .toList();

    }

    @Override
    public CustomerResponse findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneAndIsDeletedIsFalse(phoneNumber)
                .map(customerMapper::customerToCustomerResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number does not exist"));
    }


    @Override
    public CustomerResponse updateByPhone(String phone, UpdateCustomerRequest updateCustomerRequest) {

        Customer customer = customerRepository.findByPhoneAndIsDeletedIsFalse(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone number does not exist"));
        customerMapper.toCustomerPartially(updateCustomerRequest, customer);
        Customer updated = customerRepository.save(customer);
        return customerMapper.customerToCustomerResponse(updated);
    }

    @Transactional
    @Override
    public void disableByCustomerPhone(String customerPhone) {
        if(!customerRepository.existsByPhone(customerPhone)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer phone does not exist");
        }

        customerRepository.disableByCustomerPhone(customerPhone);
    }


}

