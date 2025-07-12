package co.istad.spring_boot_2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CustomerPhoneRequest(

        @NotBlank(message = "Phone number is required")
        String phoneNumber
) { }
