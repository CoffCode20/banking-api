package co.istad.spring_boot_2.exception;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse<T>(
        String message,
        Integer code,
        LocalDateTime timestamp,
        T data
) {
}