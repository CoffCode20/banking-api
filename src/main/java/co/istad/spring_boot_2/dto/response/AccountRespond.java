package co.istad.spring_boot_2.dto.response;

import co.istad.spring_boot_2.utils.CurrencyUtil;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountRespond(
        String actNo,
        String actName,
        String actCurrency,
        BigDecimal balance,
        Boolean isHide,
        String accountType
) {
}
