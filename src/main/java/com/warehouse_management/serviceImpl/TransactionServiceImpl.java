package com.warehouse_management.serviceImpl;

import com.warehouse_management.entity.*;
import com.warehouse_management.jwt.JwtFilter;
import com.warehouse_management.repositories.*;
import com.warehouse_management.requests.TransactionRequest;
import com.warehouse_management.requests.ReturnRequest;
import com.warehouse_management.responses.TransactionResponse;
import com.warehouse_management.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final GoodsRepository goodsRepository;
    private final EventRepository eventRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final JwtFilter jwtFilter;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  GoodsRepository goodsRepository,
                                  EventRepository eventRepository,
                                  DepartmentRepository departmentRepository,
                                  UserRepository userRepository,
                                  JwtFilter jwtFilter) {
        this.transactionRepository = transactionRepository;
        this.goodsRepository = goodsRepository;
        this.eventRepository = eventRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> createTransaction(TransactionRequest request) {
        try {
            String currentUser = jwtFilter.getCurrentUser();
            User issuedBy = userRepository.findByEmailId(currentUser);
            if (issuedBy == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Goods goods = goodsRepository.findById(request.getGoodsId())
                    .orElseThrow(() -> new RuntimeException("Goods not found"));

            if (goods.getQuantity() < request.getQuantity()) {
                return ResponseEntity.badRequest()
                        .body("Insufficient quantity for: " + goods.getName());
            }

            goods.setQuantity(goods.getQuantity() - request.getQuantity());
            goodsRepository.save(goods);

            Transactions transaction = new Transactions();
            transaction.setTransactionCategory(
                    Transactions.TransactionCategory.valueOf(request.getTransactionCategory().toUpperCase())
            );
            transaction.setIssuedBy(issuedBy);
            transaction.setIssueDate(LocalDateTime.now());
            transaction.setApprovedBy(request.getApprovedBy());
            transaction.setGoods(goods);
            transaction.setQuantityIssued(request.getQuantity());
            transaction.setQuantityReturned(0);
            transaction.setStatus(Transactions.TransactionStatus.ISSUED);

            if ("NORMAL".equalsIgnoreCase(request.getTransactionCategory())) {
                transaction.setReceiverName(request.getReceiverName());
                transaction.setReceiverContact(request.getReceiverContact());
                transaction.setReceiverDutyPlace(request.getReceiverDutyPlace());
            }

            if ("EVENT".equalsIgnoreCase(request.getTransactionCategory())) {
                if (request.getEventId() != null) {
                    Events event = eventRepository.findById(request.getEventId())
                            .orElseThrow(() -> new RuntimeException("Event not found"));
                    transaction.setEvent(event);
                }
                if (request.getDepartmentId() != null) {
                    Departments dept = departmentRepository.findById(request.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException("Department not found"));
                    transaction.setDepartment(dept);
                }
                transaction.setEventReceiverName(request.getEventReceiverName());
                transaction.setEventReceiverContact(request.getEventReceiverContact());
            }

            transactionRepository.save(transaction);
            return ResponseEntity.ok("Transaction created successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> returnTransaction(ReturnRequest request) {
        try {
            Transactions transaction = transactionRepository.findById(request.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            if (transaction.getStatus() != Transactions.TransactionStatus.ISSUED) {
                return ResponseEntity.badRequest().body("Transaction already returned or cancelled");
            }

            String currentUser = jwtFilter.getCurrentUser();
            User receivedBy = userRepository.findByEmailId(currentUser);
            if (receivedBy == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Goods goods = transaction.getGoods();
            if (goods != null) {
                goods.setQuantity(goods.getQuantity() + request.getQuantityReturned());
                goodsRepository.save(goods);
            }

            transaction.setQuantityReturned(request.getQuantityReturned());
            transaction.setStatus(Transactions.TransactionStatus.RETURNED);
            transaction.setReceivedBy(receivedBy);
            transaction.setReturnDate(LocalDateTime.now());
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Transaction returned successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateTransaction(Long id, TransactionRequest request) {
        try {
            Transactions transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            if (request.getApprovedBy() != null) {
                transaction.setApprovedBy(request.getApprovedBy());
            }

            if ("NORMAL".equalsIgnoreCase(transaction.getTransactionCategory().name())) {
                if (request.getReceiverName() != null)
                    transaction.setReceiverName(request.getReceiverName());
                if (request.getReceiverContact() != null)
                    transaction.setReceiverContact(request.getReceiverContact());
                if (request.getReceiverDutyPlace() != null)
                    transaction.setReceiverDutyPlace(request.getReceiverDutyPlace());
            }

            if ("EVENT".equalsIgnoreCase(transaction.getTransactionCategory().name())) {
                if (request.getEventId() != null) {
                    Events event = eventRepository.findById(request.getEventId())
                            .orElseThrow(() -> new RuntimeException("Event not found"));
                    transaction.setEvent(event);
                }
                if (request.getDepartmentId() != null) {
                    Departments dept = departmentRepository.findById(request.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException("Department not found"));
                    transaction.setDepartment(dept);
                }
                if (request.getEventReceiverName() != null)
                    transaction.setEventReceiverName(request.getEventReceiverName());
                if (request.getEventReceiverContact() != null)
                    transaction.setEventReceiverContact(request.getEventReceiverContact());
            }

            transactionRepository.save(transaction);
            return ResponseEntity.ok("Transaction updated successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteTransaction(Long id) {
        try {
            Transactions transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            Goods goods = transaction.getGoods();
            if (goods != null && transaction.getQuantityIssued() != null) {
                goods.setQuantity(goods.getQuantity() + transaction.getQuantityIssued());
                goodsRepository.save(goods);
            }

            transactionRepository.delete(transaction);
            return ResponseEntity.ok("Transaction deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<TransactionResponse> getTransactionById(Long id) {
        Transactions t = transactionRepository.findById(id).orElse(null);
        if (t == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapToResponse(t));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<Transactions> list = transactionRepository.findAll();
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByCategory(String category) {
        Transactions.TransactionCategory cat = Transactions.TransactionCategory.valueOf(category.toUpperCase());
        List<Transactions> list = transactionRepository.findByTransactionCategory(cat);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventId(Long eventId) {
        List<Transactions> list = transactionRepository.findByEventId(eventId);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventName(String eventName) {
        List<Transactions> list = transactionRepository.findByEvent_EventNameContainingIgnoreCase(eventName);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentId(Long departmentId) {
        List<Transactions> list = transactionRepository.findByDepartmentId(departmentId);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentName(String departmentName) {
        List<Transactions> list = transactionRepository.findByDepartment_DepartmentNameContainingIgnoreCase(departmentName);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverName(String name) {
        List<Transactions> list = transactionRepository.findByReceiverNameContainingIgnoreCase(name);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverContact(String contact) {
        List<Transactions> list = transactionRepository.findByReceiverContact(contact);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverName(String name) {
        List<Transactions> list = transactionRepository.findByEventReceiverNameContainingIgnoreCase(name);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverContact(String contact) {
        List<Transactions> list = transactionRepository.findByEventReceiverContact(contact);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssuedBy(Long userId) {
        List<Transactions> list = transactionRepository.findByIssuedById(userId);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceivedBy(Long userId) {
        List<Transactions> list = transactionRepository.findByReceivedById(userId);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByApprovedBy(String approvedBy) {
        List<Transactions> list = transactionRepository.findByApprovedByContainingIgnoreCase(approvedBy);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssueDateRange(LocalDateTime start, LocalDateTime end) {
        List<Transactions> list = transactionRepository.findByIssueDateBetween(start, end);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReturnDateRange(LocalDateTime start, LocalDateTime end) {
        List<Transactions> list = transactionRepository.findByReturnDateBetween(start, end);
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    private TransactionResponse mapToResponse(Transactions t) {
        TransactionResponse response = new TransactionResponse();
        response.setId(t.getId());
        response.setTransactionCategory(t.getTransactionCategory() != null ? t.getTransactionCategory().name() : null);
        response.setStatus(t.getStatus() != null ? t.getStatus().name() : null);
        response.setIssuedById(t.getIssuedBy() != null ? t.getIssuedBy().getId() : null);
        response.setIssuedByName(t.getIssuedBy() != null ? t.getIssuedBy().getFullName() : null);
        response.setReceivedById(t.getReceivedBy() != null ? t.getReceivedBy().getId() : null);
        response.setReceivedByName(t.getReceivedBy() != null ? t.getReceivedBy().getFullName() : null);
        response.setApprovedBy(t.getApprovedBy());
        response.setGoodsId(t.getGoods() != null ? t.getGoods().getId() : null);
        response.setGoodsName(t.getGoods() != null ? t.getGoods().getName() : null);
        response.setQuantityIssued(t.getQuantityIssued());
        response.setQuantityReturned(t.getQuantityReturned());
        response.setIssueDate(t.getIssueDate());
        response.setReturnDate(t.getReturnDate());
        response.setReceiverName(t.getReceiverName());
        response.setReceiverContact(t.getReceiverContact());
        response.setReceiverDutyPlace(t.getReceiverDutyPlace());
        response.setEventId(t.getEvent() != null ? t.getEvent().getId() : null);
        response.setEventName(t.getEvent() != null ? t.getEvent().getEventName() : null);
        response.setDepartmentId(t.getDepartment() != null ? t.getDepartment().getId() : null);
        response.setDepartmentName(t.getDepartment() != null ? t.getDepartment().getDepartmentName() : null);
        response.setEventReceiverName(t.getEventReceiverName());
        response.setEventReceiverContact(t.getEventReceiverContact());
        response.setCreatedAt(t.getCreatedAt());
        return response;
    }
}