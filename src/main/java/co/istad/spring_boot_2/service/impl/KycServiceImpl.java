package co.istad.spring_boot_2.service.impl;

import co.istad.spring_boot_2.domain.KYC;
import co.istad.spring_boot_2.repository.KYCRepository;
import co.istad.spring_boot_2.service.KycService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KycServiceImpl implements KycService {

    private final KYCRepository kycRepository;

    @Override
    public void verifyKycCustomer(String nationalCardId) {
        KYC kyc = kycRepository.findKYCByNationalCodeId(nationalCardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kyc not found"));
        log.info("Kyc Customer with NationalCode {} found", nationalCardId);
        kyc.setIsVerified(true);
        kycRepository.save(kyc);
    }
}
