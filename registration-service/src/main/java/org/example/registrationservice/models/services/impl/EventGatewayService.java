package org.example.registrationservice.models.services.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.registrationservice.clients.EventClient;
import org.example.registrationservice.exceptions.EventNotFoundException;
import org.example.registrationservice.exceptions.EventServiceException;
import org.example.registrationservice.models.dto.responses.EventResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventGatewayService {
    private final EventClient eventClient;

    @CircuitBreaker(name = "registrationService", fallbackMethod = "fallbackGetEventById")
    public EventResponse getEventById(Long eventId) {
        try {
            EventResponse response = eventClient.getEventById(eventId);
            if (response == null || response.id() == null) {
                throw new EventNotFoundException(eventId);
            }
            return response;
        } catch (feign.FeignException.NotFound e) {
            throw new EventNotFoundException(eventId);
        } catch (feign.FeignException e) {
            if (e.status() == 404) {
                throw new EventNotFoundException(eventId);
            }
            throw new EventServiceException("Event service call failed with status: " + e.status(), e);
        }
    }

    public EventResponse fallbackGetEventById(Long eventId, Throwable throwable) {
        log.error("CircuitBreaker fallback triggered for eventId {}: {}", eventId, throwable.getMessage());
        if (throwable instanceof EventNotFoundException) {
            throw (EventNotFoundException) throwable;
        }
        if (throwable instanceof feign.FeignException.NotFound) {
            throw new EventNotFoundException(eventId);
        }
        if (throwable instanceof feign.FeignException && ((feign.FeignException) throwable).status() == 404) {
            throw new EventNotFoundException(eventId);
        }
        throw new EventServiceException("Event service is unavailable or encountered an error", throwable);
    }
}
