package org.example.notifyservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.notifyservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegistrationCreatedConsumer {

    private final EmailService emailService;

    public RegistrationCreatedConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${notification.kafka.order-created-topic:registration-created}")
    public void consume(String email) {
        if (email == null) {
            log.warn("Received null message from Kafka topic");
            return;
        }

        String cleanedEmail = email.trim();
        // Remove surrounding quotes if serialized with JSON quotes
        if (cleanedEmail.startsWith("\"") && cleanedEmail.endsWith("\"") && cleanedEmail.length() >= 2) {
            cleanedEmail = cleanedEmail.substring(1, cleanedEmail.length() - 1).trim();
        }

        if (cleanedEmail.isBlank() || !cleanedEmail.contains("@")) {
            log.warn("Invalid email address received: {}", email);
            return;
        }

        log.info("Processing registration notification for email: {}", cleanedEmail);
        emailService.sendOrderCreatedEmail(cleanedEmail);
    }
}
