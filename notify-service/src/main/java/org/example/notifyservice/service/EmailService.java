package org.example.notifyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${notification.mail.from:${spring.mail.username:}}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendOrderCreatedEmail(String recipient) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (from != null && !from.isBlank()) {
                message.setFrom(from);
            }
            message.setTo(recipient);
            message.setSubject("Registration Confirmation");
            message.setText("Your event registration has been created successfully!");
            mailSender.send(message);
            log.info("Successfully sent registration confirmation email to: {}", recipient);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", recipient, e.getMessage());
        }
    }
}
