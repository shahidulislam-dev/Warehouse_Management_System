package com.warehouse_management.controllers;

import com.warehouse_management.requests.TransactionRequest;
import com.warehouse_management.requests.ReturnRequest;
import com.warehouse_management.responses.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/transactions")
public interface TransactionController {

    @PostMapping("/create")
    ResponseEntity<String> create(@RequestBody TransactionRequest request);

    @PostMapping("/return")
    ResponseEntity<String> returnTransaction(@RequestBody ReturnRequest request);

    @PutMapping("/update/{id}")
    ResponseEntity<String> update(@PathVariable Long id, @RequestBody TransactionRequest request);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id);

    @GetMapping("/{id}")
    ResponseEntity<TransactionResponse> getById(@PathVariable Long id);

    @GetMapping("/all")
    ResponseEntity<List<TransactionResponse>> getAll();

    @GetMapping("/category/{category}")
    ResponseEntity<List<TransactionResponse>> getByCategory(@PathVariable String category);

    @GetMapping("/event/{eventId}")
    ResponseEntity<List<TransactionResponse>> getByEventId(@PathVariable Long eventId);

    @GetMapping("/event-name/{eventName}")
    ResponseEntity<List<TransactionResponse>> getByEventName(@PathVariable String eventName);

    @GetMapping("/department/{departmentId}")
    ResponseEntity<List<TransactionResponse>> getByDepartmentId(@PathVariable Long departmentId);

    @GetMapping("/department-name/{departmentName}")
    ResponseEntity<List<TransactionResponse>> getByDepartmentName(@PathVariable String departmentName);

    @GetMapping("/receiver-name/{name}")
    ResponseEntity<List<TransactionResponse>> getByReceiverName(@PathVariable String name);

    @GetMapping("/receiver-contact/{contact}")
    ResponseEntity<List<TransactionResponse>> getByReceiverContact(@PathVariable String contact);

    @GetMapping("/event-receiver-name/{name}")
    ResponseEntity<List<TransactionResponse>> getByEventReceiverName(@PathVariable String name);

    @GetMapping("/event-receiver-contact/{contact}")
    ResponseEntity<List<TransactionResponse>> getByEventReceiverContact(@PathVariable String contact);

    @GetMapping("/issued-by/{userId}")
    ResponseEntity<List<TransactionResponse>> getByIssuedBy(@PathVariable Long userId);

    @GetMapping("/received-by/{userId}")
    ResponseEntity<List<TransactionResponse>> getByReceivedBy(@PathVariable Long userId);

    @GetMapping("/approved-by/{approvedBy}")
    ResponseEntity<List<TransactionResponse>> getByApprovedBy(@PathVariable String approvedBy);

    @GetMapping("/issue-date")
    ResponseEntity<List<TransactionResponse>> getByIssueDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    );

    @GetMapping("/return-date")
    ResponseEntity<List<TransactionResponse>> getByReturnDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    );
}