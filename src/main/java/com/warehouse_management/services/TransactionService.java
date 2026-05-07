package com.warehouse_management.services;

import com.warehouse_management.requests.TransactionRequest;
import com.warehouse_management.requests.ReturnRequest;
import com.warehouse_management.responses.TransactionResponse;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    ResponseEntity<String> createTransaction(TransactionRequest request);

    ResponseEntity<String> returnTransaction(ReturnRequest request);

    ResponseEntity<String> updateTransaction(Long id, TransactionRequest request);

    ResponseEntity<String> deleteTransaction(Long id);

    ResponseEntity<TransactionResponse> getTransactionById(Long id);

    ResponseEntity<List<TransactionResponse>> getAllTransactions();

    // Filters
    ResponseEntity<List<TransactionResponse>> getByCategory(String category);
    ResponseEntity<List<TransactionResponse>> getByEventId(Long eventId);
    ResponseEntity<List<TransactionResponse>> getByEventName(String eventName);
    ResponseEntity<List<TransactionResponse>> getByDepartmentId(Long departmentId);
    ResponseEntity<List<TransactionResponse>> getByDepartmentName(String departmentName);
    ResponseEntity<List<TransactionResponse>> getByReceiverName(String name);
    ResponseEntity<List<TransactionResponse>> getByReceiverContact(String contact);
    ResponseEntity<List<TransactionResponse>> getByEventReceiverName(String name);
    ResponseEntity<List<TransactionResponse>> getByEventReceiverContact(String contact);
    ResponseEntity<List<TransactionResponse>> getByIssuedBy(Long userId);
    ResponseEntity<List<TransactionResponse>> getByReceivedBy(Long userId);
    ResponseEntity<List<TransactionResponse>> getByApprovedBy(String approvedBy);
    ResponseEntity<List<TransactionResponse>> getByIssueDateRange(LocalDateTime start, LocalDateTime end);
    ResponseEntity<List<TransactionResponse>> getByReturnDateRange(LocalDateTime start, LocalDateTime end);
}
