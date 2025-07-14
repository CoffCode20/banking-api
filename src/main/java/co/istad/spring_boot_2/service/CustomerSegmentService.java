package co.istad.spring_boot_2.service;

import co.istad.spring_boot_2.dto.request.CreateCustomerSegmentRequest;
import co.istad.spring_boot_2.dto.response.CustomerSegmentResponse;

public interface CustomerSegmentService {

    CustomerSegmentResponse createCustomerSegment(CreateCustomerSegmentRequest request);
}
