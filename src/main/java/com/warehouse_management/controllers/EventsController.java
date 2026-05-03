package com.warehouse_management.controllers;

import com.warehouse_management.requests.EventsRequest;
import com.warehouse_management.responses.EventsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/events")
public interface EventsController {

    @PostMapping("/create")
    ResponseEntity<String> create(@RequestBody EventsRequest request);

    @PutMapping("/update/{id}")
    ResponseEntity<String> update(@PathVariable Long id, @RequestBody EventsRequest request);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id);

    @GetMapping("/{id}")
    ResponseEntity<EventsResponse> getById(@PathVariable Long id);

    @GetMapping("/all")
    ResponseEntity<List<EventsResponse>> getAll();

    @GetMapping("/name/{eventName}")
    ResponseEntity<List<EventsResponse>> getByName(@PathVariable String eventName);

    @GetMapping("/active")
    ResponseEntity<List<EventsResponse>> getActive();
}