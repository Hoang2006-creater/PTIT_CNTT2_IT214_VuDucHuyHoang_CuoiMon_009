package org.example.registrationservice.models.repositories;

import org.example.registrationservice.models.entities.RegistrationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationDetailRepository extends JpaRepository<RegistrationDetail, Long> {
}
