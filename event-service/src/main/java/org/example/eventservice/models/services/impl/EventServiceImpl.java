package org.example.eventservice.models.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.eventservice.exceptions.EventNotFoundException;
import org.example.eventservice.models.entities.Event;
import org.example.eventservice.models.repositories.EventRepository;
import org.example.eventservice.models.services.EventService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    @Cacheable(value = "events", key = "#id", sync = true)
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }
}
