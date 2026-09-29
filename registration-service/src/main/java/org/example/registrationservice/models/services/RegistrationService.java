package org.example.registrationservice.models.services;

import org.example.registrationservice.models.dto.requests.CreateRegistrationRequest;
import org.example.registrationservice.models.dto.responses.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse createRegistration(CreateRegistrationRequest request);
}
