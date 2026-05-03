package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.TransactionController;
import com.warehouse_management.requests.TransactionRequest;
import com.warehouse_management.requests.ReturnRequest;
import com.warehouse_management.responses.TransactionResponse;
import com.warehouse_management.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;

    public TransactionControllerImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<String> create(TransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @Override
    public ResponseEntity<String> returnTransaction(ReturnRequest request) {
        return transactionService.returnTransaction(request);
    }

    @Override
    public ResponseEntity<String> update(Long id, TransactionRequest request) {
        return transactionService.updateTransaction(id, request);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        return transactionService.deleteTransaction(id);
    }

    @Override
    public ResponseEntity<TransactionResponse> getById(Long id) {
        return transactionService.getTransactionById(id);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return transactionService.getAllTransactions();
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByCategory(String category) {
        return transactionService.getByCategory(category);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventId(Long eventId) {
        return transactionService.getByEventId(eventId);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventName(String eventName) {
        return transactionService.getByEventName(eventName);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentId(Long departmentId) {
        return transactionService.getByDepartmentId(departmentId);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentName(String departmentName) {
        return transactionService.getByDepartmentName(departmentName);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverName(String name) {
        return transactionService.getByReceiverName(name);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverContact(String contact) {
        return transactionService.getByReceiverContact(contact);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverName(String name) {
        return transactionService.getByEventReceiverName(name);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverContact(String contact) {
        return transactionService.getByEventReceiverContact(contact);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssuedBy(Long userId) {
        return transactionService.getByIssuedBy(userId);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceivedBy(Long userId) {
        return transactionService.getByReceivedBy(userId);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByApprovedBy(String approvedBy) {
        return transactionService.getByApprovedBy(approvedBy);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssueDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionService.getByIssueDateRange(start, end);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReturnDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionService.getByReturnDateRange(start, end);
    }
}