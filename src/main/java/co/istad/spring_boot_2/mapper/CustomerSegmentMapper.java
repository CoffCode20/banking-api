package co.istad.spring_boot_2.mapper;

import co.istad.spring_boot_2.domain.CustomerSegment;
import co.istad.spring_boot_2.dto.request.CreateCustomerSegmentRequest;
import co.istad.spring_boot_2.dto.response.CustomerSegmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerSegmentMapper {

    @Mapping(target = "segmentName", expression = "java(createCustomerSegmentRequest.segmentName().toUpperCase())")
    CustomerSegment customerSegmentRequestToCustomerSegment(CreateCustomerSegmentRequest createCustomerSegmentRequest);

    CustomerSegmentResponse toCustomerSegmentResponse(CustomerSegment customerSegment);

}
