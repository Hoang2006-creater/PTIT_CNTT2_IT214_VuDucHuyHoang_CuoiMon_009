package org.example.registrationservice.models.dto.responses;

public record EventResponse(
        Long id,

        String eventName,

        Double ticketPrice,

        Integer availableTickets
) {
}
