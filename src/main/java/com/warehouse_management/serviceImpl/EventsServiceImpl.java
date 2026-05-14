package com.warehouse_management.serviceImpl;

import com.warehouse_management.entity.Events;
import com.warehouse_management.repositories.EventRepository;
import com.warehouse_management.requests.EventsRequest;
import com.warehouse_management.responses.EventsResponse;
import com.warehouse_management.services.EventsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventsServiceImpl implements EventsService {

    private final EventRepository eventRepository;

    public EventsServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<String> createEvent(EventsRequest request) {
        try {
            Events event = new Events();
            event.setEventName(request.getEventName());
            event.setEventDate(request.getEventDate());
            event.setActive(request.isActive());
            // ✅ createdAt is auto-set by @CreationTimestamp

            eventRepository.save(event);
            return ResponseEntity.ok("Event created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateEvent(Long id, EventsRequest request) {
        try {
            Optional<Events> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isEmpty()) {
                return ResponseEntity.badRequest().body("Event not found");
            }

            Events event = optionalEvent.get();

            if (request.getEventName() != null) {
                event.setEventName(request.getEventName());
            }
            if (request.getEventDate() != null) {
                event.setEventDate(request.getEventDate());
            }
            event.setActive(request.isActive());
            // ✅ createdAt remains unchanged on update

            eventRepository.save(event);
            return ResponseEntity.ok("Event updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteEvent(Long id) {
        try {
            Optional<Events> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isEmpty()) {
                return ResponseEntity.badRequest().body("Event not found");
            }

            eventRepository.deleteById(id);
            return ResponseEntity.ok("Event deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<EventsResponse> getEventById(Long id) {
        try {
            Optional<Events> optionalEvent = eventRepository.findById(id);
            if (optionalEvent.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Events event = optionalEvent.get();
            return ResponseEntity.ok(mapToResponse(event));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<EventsResponse>> getAllEvents() {
        try {
            List<Events> events = eventRepository.findAll();
            List<EventsResponse> responses = new ArrayList<>();
            for (Events event : events) {
                responses.add(mapToResponse(event));
            }
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<EventsResponse>> getEventsByName(String eventName) {
        try {
            List<Events> events = eventRepository.findByEventNameContainingIgnoreCase(eventName);
            List<EventsResponse> responses = new ArrayList<>();
            for (Events event : events) {
                responses.add(mapToResponse(event));
            }
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<EventsResponse>> getActiveEvents() {
        try {
            List<Events> events = eventRepository.findByActiveTrue();
            List<EventsResponse> responses = new ArrayList<>();
            for (Events event : events) {
                responses.add(mapToResponse(event));
            }
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private EventsResponse mapToResponse(Events event) {
        EventsResponse response = new EventsResponse();
        response.setId(event.getId());
        response.setEventName(event.getEventName());
        response.setEventDate(event.getEventDate());
        response.setActive(event.isActive());
        response.setCreatedAt(event.getCreatedAt());  // ✅ Include createdAt in response
        return response;
    }
}