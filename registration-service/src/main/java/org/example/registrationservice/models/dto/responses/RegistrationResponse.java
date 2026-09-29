package org.example.registrationservice.models.dto.responses;

import org.example.registrationservice.models.constants.RegistrationStatus;

import java.util.List;

public record RegistrationResponse(
        Long id,

        String participantName,

        String participantEmail,

        Double totalAmount,

        RegistrationStatus status,

        List<RegistrationDetailResponse> details
) {
}
