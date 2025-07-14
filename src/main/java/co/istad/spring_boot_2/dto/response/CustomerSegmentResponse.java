package co.istad.spring_boot_2.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerSegmentResponse(
        String segmentName,
        BigDecimal overLimitSet,
        String description)
{ }
