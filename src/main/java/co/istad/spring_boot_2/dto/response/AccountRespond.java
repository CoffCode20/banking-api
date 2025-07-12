package co.istad.spring_boot_2.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountRespond(
        String actName,

        String actNo,

        BigDecimal balance,

        BigDecimal overLimit,

        String accountType
) {
}
