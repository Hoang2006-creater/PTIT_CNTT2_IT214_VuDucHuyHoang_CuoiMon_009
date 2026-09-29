package org.example.eventservice.models.services;

import org.example.eventservice.models.entities.Event;

public interface EventService {
    Event getEventById(Long id);
}
