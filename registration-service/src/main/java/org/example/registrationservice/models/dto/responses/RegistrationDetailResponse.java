package org.example.registrationservice.models.dto.responses;

public record RegistrationDetailResponse(
        Long id,

        Long eventId,

        Integer quantity,

        Double ticketPrice,

        Double lineTotal
) {
}
