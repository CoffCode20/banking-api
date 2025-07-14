package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.CustomerSegment;
import co.istad.spring_boot_2.dto.request.CreateCustomerSegmentRequest;
import co.istad.spring_boot_2.dto.response.CustomerSegmentResponse;
import co.istad.spring_boot_2.mapper.CustomerSegmentMapper;
import co.istad.spring_boot_2.repository.CustomerSegmentRepository;
import co.istad.spring_boot_2.service.CustomerSegmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerCustomerSegmentServiceImpl implements CustomerSegmentService {

    private final CustomerSegmentRepository customerSegmentRepository;
    private final CustomerSegmentMapper customerSegmentMapper;

    @Override
    public CustomerSegmentResponse createCustomerSegment(CreateCustomerSegmentRequest request) {
        if(!customerSegmentRepository.existsBySegmentName(request.segmentName())){
            CustomerSegment customerSegment = customerSegmentMapper.customerSegmentRequestToCustomerSegment(request);
            customerSegment.setIsDeleted(false);
            return customerSegmentMapper.toCustomerSegmentResponse(customerSegmentRepository.save(customerSegment));
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Segment already exists");
    }
}
