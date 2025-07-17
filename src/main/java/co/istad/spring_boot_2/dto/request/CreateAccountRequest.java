package co.istad.spring_boot_2.dto.request;

import co.istad.spring_boot_2.utils.CurrencyUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;

@Builder
public record CreateAccountRequest(

        String actNo,

        @NotBlank(message = "Account name is required")
        String actName,

        CurrencyUtil actCurrency,

        @Positive(message = "Account balance must be greater than 10$ or 40,000KHR")
        BigDecimal balance,

        @NotBlank(message = "Account type is required")
        String accountType,

        @NotBlank(message = "Customer phone number is required")
        String phone
) {
}
