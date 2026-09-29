package org.example.registrationservice.models.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.registrationservice.exceptions.EventNotFoundException;
import org.example.registrationservice.models.constants.RegistrationStatus;
import org.example.registrationservice.models.dto.requests.CreateRegistrationDetailRequest;
import org.example.registrationservice.models.dto.requests.CreateRegistrationRequest;
import org.example.registrationservice.models.dto.responses.EventResponse;
import org.example.registrationservice.models.dto.responses.RegistrationDetailResponse;
import org.example.registrationservice.models.dto.responses.RegistrationResponse;
import org.example.registrationservice.models.entities.Registration;
import org.example.registrationservice.models.entities.RegistrationDetail;
import org.example.registrationservice.models.repositories.RegistrationDetailRepository;
import org.example.registrationservice.models.repositories.RegistrationRepository;
import org.example.registrationservice.models.services.RegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RegistrationDetailRepository registrationDetailRepository;
    private final EventGatewayService eventGatewayService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${notification.kafka.order-created-topic:registration-created}")
    private String orderCreatedTopic;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegistrationResponse createRegistration(CreateRegistrationRequest request) {
        double totalAmount = 0.0;
        List<RegistrationDetail> details = new ArrayList<>();

        for (CreateRegistrationDetailRequest item : request.items()) {
            EventResponse event = eventGatewayService.getEventById(item.eventId());
            if (event == null || event.ticketPrice() == null) {
                throw new EventNotFoundException(item.eventId());
            }
            double price = event.ticketPrice();
            double lineTotal = price * item.quantity();
            totalAmount += lineTotal;

            RegistrationDetail detail = RegistrationDetail.builder()
                    .eventId(item.eventId())
                    .quantity(item.quantity())
                    .ticketPrice(price)
                    .lineTotal(lineTotal)
                    .build();
            details.add(detail);
        }

        Registration registration = Registration.builder()
                .participantName(request.participantName())
                .participantEmail(request.participantEmail())
                .totalAmount(totalAmount)
                .status(RegistrationStatus.PENDING)
                .build();

        Registration savedRegistration = registrationRepository.save(registration);

        for (RegistrationDetail detail : details) {
            detail.setRegistration(savedRegistration);
        }
        List<RegistrationDetail> savedDetails = registrationDetailRepository.saveAll(details);

        try {
            kafkaTemplate.send(orderCreatedTopic, request.participantEmail());
            log.info("Sent registration message to Kafka topic {}: {}", orderCreatedTopic, request.participantEmail());
        } catch (Exception e) {
            log.error("Failed to send Kafka message to topic {}: {}", orderCreatedTopic, e.getMessage());
        }

        List<RegistrationDetailResponse> detailResponses = savedDetails.stream()
                .map(d -> new RegistrationDetailResponse(
                        d.getId(),
                        d.getEventId(),
                        d.getQuantity(),
                        d.getTicketPrice(),
                        d.getLineTotal()
                ))
                .toList();

        return new RegistrationResponse(
                savedRegistration.getId(),
                savedRegistration.getParticipantName(),
                savedRegistration.getParticipantEmail(),
                savedRegistration.getTotalAmount(),
                savedRegistration.getStatus(),
                detailResponses
        );
    }
}
