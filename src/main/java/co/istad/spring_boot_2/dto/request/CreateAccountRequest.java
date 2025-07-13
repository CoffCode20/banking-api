package co.istad.spring_boot_2.dto.request;

import co.istad.spring_boot_2.domain.AccountType;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateAccountRequest(

        @NotBlank(message = "Account name is required")
        String actName,

        String actNo,

        @NotBlank(message = "Account phone number is required")
        @Size(min = 9, max = 12)
        String phoneNumber,

        @NotNull(message = "Balance is required")
        @DecimalMin(value = "5.0", message = "Minimum balance must be at least 5")
        BigDecimal balance,

        @NotBlank(message = "Account type is required")
        String accountType
) {
}
