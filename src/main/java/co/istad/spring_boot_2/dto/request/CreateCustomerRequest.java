package co.istad.spring_boot_2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateCustomerRequest(

        @NotBlank(message = "Customer name is required")
        String fullName,

        @NotBlank(message = "Customer gender is required")
        String gender,

        @NotBlank(message = "Customer dob is required")
        String dob,

        @NotBlank(message = "Customer email is required")
        @Email
        String email,

        @NotBlank(message = "Customer phone number is required")
        @Size(min = 9, max = 11)
        String phone,

        String remark,

        @NotBlank(message = "Customer national card id is required")
        @Size(min = 11, max = 12)
        String nationalCardId,

        @NotBlank(message = "Customer segment is required")
        String customerSegment
) {
}
