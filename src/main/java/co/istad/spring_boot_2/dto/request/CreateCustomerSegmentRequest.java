package co.istad.spring_boot_2.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateCustomerSegmentRequest(
        @NotBlank(message = "Segment is required")
        String segmentName,

        @NotNull(message = "overLimitSet is required")
        @Positive
        BigDecimal overLimitSet,

        String description
) { }
