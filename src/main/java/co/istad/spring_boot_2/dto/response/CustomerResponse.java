package co.istad.spring_boot_2.dto.response;

import lombok.Builder;

@Builder
public record CustomerResponse(
        String fullName,
        String gender,
        String email,
        String phone
) {
}
