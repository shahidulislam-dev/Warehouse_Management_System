package com.warehouse_management.services;

import com.warehouse_management.requests.EventsRequest;
import com.warehouse_management.responses.EventsResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface EventsService {

    ResponseEntity<String> createEvent(EventsRequest request);

    ResponseEntity<String> updateEvent(Long id, EventsRequest request);

    ResponseEntity<String> deleteEvent(Long id);

    ResponseEntity<EventsResponse> getEventById(Long id);

    ResponseEntity<List<EventsResponse>> getAllEvents();

    ResponseEntity<List<EventsResponse>> getEventsByName(String eventName);

    ResponseEntity<List<EventsResponse>> getActiveEvents();
}