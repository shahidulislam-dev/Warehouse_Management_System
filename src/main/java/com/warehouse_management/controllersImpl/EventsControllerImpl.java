package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.EventsController;
import com.warehouse_management.requests.EventsRequest;
import com.warehouse_management.responses.EventsResponse;
import com.warehouse_management.services.EventsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventsControllerImpl implements EventsController {

    private final EventsService eventsService;

    public EventsControllerImpl(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @Override
    public ResponseEntity<String> create(EventsRequest request) {
        return eventsService.createEvent(request);
    }

    @Override
    public ResponseEntity<String> update(Long id, EventsRequest request) {
        return eventsService.updateEvent(id, request);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        return eventsService.deleteEvent(id);
    }

    @Override
    public ResponseEntity<EventsResponse> getById(Long id) {
        return eventsService.getEventById(id);
    }

    @Override
    public ResponseEntity<List<EventsResponse>> getAll() {
        return eventsService.getAllEvents();
    }

    @Override
    public ResponseEntity<List<EventsResponse>> getByName(String eventName) {
        return eventsService.getEventsByName(eventName);
    }

    @Override
    public ResponseEntity<List<EventsResponse>> getActive() {
        return eventsService.getActiveEvents();
    }
}