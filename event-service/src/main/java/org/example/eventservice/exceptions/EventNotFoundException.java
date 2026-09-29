package org.example.eventservice.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event not found with ID: " + id);

    }
}
