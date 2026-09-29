package org.example.registrationservice.models.repositories;

import org.example.registrationservice.models.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
}
