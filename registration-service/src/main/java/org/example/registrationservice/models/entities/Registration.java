package org.example.registrationservice.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.registrationservice.models.constants.RegistrationStatus;

@Entity
@Table(name = "registrations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "participant_name", nullable = false)
    private String participantName;

    @Column(name = "participant_email", nullable = false)
    private String participantEmail;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus status;
}