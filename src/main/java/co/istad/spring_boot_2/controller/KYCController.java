package co.istad.spring_boot_2.controller;

import co.istad.spring_boot_2.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kyc")
public class KYCController {
    private final KycService kycService;

    @PutMapping("/{nationalCodeId}")
    public ResponseEntity<String> verifyKycCustomer(@PathVariable String nationalCodeId) {
        kycService.verifyKycCustomer(nationalCodeId);
        return ResponseEntity.status(HttpStatus.OK).body("Customer " + nationalCodeId + " is already verified");
    }
}
