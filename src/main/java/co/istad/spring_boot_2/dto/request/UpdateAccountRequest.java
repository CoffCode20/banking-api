package co.istad.spring_boot_2.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateAccountRequest(

        String actName,

        BigDecimal balance
) { }
