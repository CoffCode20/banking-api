package co.istad.spring_boot_2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateCustomerRequest(

        String fullName,

        String gender,

        String remark
) { }
