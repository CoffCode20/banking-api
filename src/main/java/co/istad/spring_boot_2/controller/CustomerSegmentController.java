package co.istad.spring_boot_2.controller;

import co.istad.spring_boot_2.domain.CustomerSegment;
import co.istad.spring_boot_2.dto.request.CreateCustomerSegmentRequest;
import co.istad.spring_boot_2.dto.response.CustomerSegmentResponse;
import co.istad.spring_boot_2.service.CustomerSegmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/segment")
@RequiredArgsConstructor
public class CustomerSegmentController {
    private final CustomerSegmentService customerSegmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerSegmentResponse createCustomerSegment(@Valid @RequestBody CreateCustomerSegmentRequest request) {
        return customerSegmentService.createCustomerSegment(request);
    }

}
