package co.istad.spring_boot_2.mapper;

import co.istad.spring_boot_2.domain.Customer;
import co.istad.spring_boot_2.dto.request.CreateCustomerRequest;
import co.istad.spring_boot_2.dto.response.CustomerResponse;
import co.istad.spring_boot_2.dto.request.UpdateCustomerRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    // source (params)
    // target (return_type)

    @Mapping(target = "customerSegment", ignore = true)
    @Mapping(target = "kyc", ignore = true)
    Customer customerRequestToCustomer(CreateCustomerRequest createCustomerRequest);

    @Mapping(target = "customerSegment", source = "customer.customerSegment.segmentName")
    CustomerResponse customerToCustomerResponse(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toCustomerPartially(UpdateCustomerRequest updateCustomerRequest,@MappingTarget Customer customer);
}
