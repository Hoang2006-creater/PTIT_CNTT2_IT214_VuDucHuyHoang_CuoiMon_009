package org.example.registrationservice.models.dto.requests;

import jakarta.validation.constraints.*;

public record CreateRegistrationDetailRequest(
        @NotNull(message = "Event ID không được để trống")
        Long eventId,

        @NotNull(message = "Số lượng vé không được để trống")
        @Positive(message = "Số lượng vé phải lớn hơn 0")
        Integer quantity
) {
}
