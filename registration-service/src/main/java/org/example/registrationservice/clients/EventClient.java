package org.example.registrationservice.clients;

import org.example.registrationservice.models.dto.responses.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", path = "/api/v1/events")
public interface EventClient {

    @GetMapping("/{id}")
    EventResponse getEventById(@PathVariable Long id);

}
