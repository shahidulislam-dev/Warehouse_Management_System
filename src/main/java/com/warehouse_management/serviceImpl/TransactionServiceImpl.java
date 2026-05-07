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
    private final TransactionItemRepository transactionItemRepository;
    private final GoodsRepository goodsRepository;
    private final EventRepository eventRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final JwtFilter jwtFilter;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  TransactionItemRepository transactionItemRepository,
                                  GoodsRepository goodsRepository,
                                  EventRepository eventRepository,
                                  DepartmentRepository departmentRepository,
                                  UserRepository userRepository,
                                  JwtFilter jwtFilter) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.goodsRepository = goodsRepository;
        this.eventRepository = eventRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.jwtFilter = jwtFilter;
    }

    // ==================== CREATE ====================
    @Override
    public ResponseEntity<String> createTransaction(TransactionRequest request) {
        try {
            String currentUser = jwtFilter.getCurrentUser();
            User issuedBy = userRepository.findByEmailId(currentUser);
            if (issuedBy == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Transactions transaction = new Transactions();
            transaction.setTransactionCategory(
                    Transactions.TransactionCategory.valueOf(request.getTransactionCategory().toUpperCase())
            );
            transaction.setIssuedBy(issuedBy);
            transaction.setIssueDate(LocalDateTime.now());
            transaction.setApprovedBy(request.getApprovedBy());
            transaction.setStatus(Transactions.TransactionStatus.ISSUED);

            // Set category-specific details
            setCategoryDetails(transaction, request);

            // Save multiple items
            List<TransactionsItems> items = new ArrayList<>();
            for (TransactionRequest.ItemRequest itemReq : request.getItems()) {
                Goods goods = goodsRepository.findById(itemReq.getGoodsId())
                        .orElseThrow(() -> new RuntimeException("Goods not found: " + itemReq.getGoodsId()));

                if (goods.getQuantity() < itemReq.getQuantity()) {
                    return ResponseEntity.badRequest()
                            .body("Insufficient quantity for: " + goods.getName());
                }

                goods.setQuantity(goods.getQuantity() - itemReq.getQuantity());
                goodsRepository.save(goods);

                TransactionsItems item = new TransactionsItems();
                item.setTransactions(transaction);
                item.setGoods(goods);
                item.setQuantity(itemReq.getQuantity());
                item.setQuantityReturned(0);
                item.setStatus(TransactionsItems.ItemStatus.ISSUED);
                items.add(item);
            }
            transaction.setTransactionItems(items);

            transactionRepository.save(transaction);
            return ResponseEntity.ok("Transaction created successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // ==================== RETURN ====================
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

            boolean allReturned = true;

            for (ReturnRequest.ReturnItem returnItem : request.getItems()) {
                TransactionsItems item = transactionItemRepository.findById(returnItem.getTransactionItemId())
                        .orElseThrow(() -> new RuntimeException("Item not found"));

                // Return to stock
                Goods goods = item.getGoods();
                goods.setQuantity(goods.getQuantity() + returnItem.getQuantityReturned());
                goodsRepository.save(goods);

                // Update item
                int newReturned = item.getQuantityReturned() + returnItem.getQuantityReturned();
                item.setQuantityReturned(newReturned);

                if (newReturned >= item.getQuantity()) {
                    item.setStatus(TransactionsItems.ItemStatus.RETURNED);
                } else {
                    item.setStatus(TransactionsItems.ItemStatus.ISSUED);
                    allReturned = false;
                }
                transactionItemRepository.save(item);
            }

            if (allReturned) {
                transaction.setStatus(Transactions.TransactionStatus.RETURNED);
            }
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

    // ==================== UPDATE ====================
    @Override
    public ResponseEntity<String> updateTransaction(Long id, TransactionRequest request) {
        try {
            Transactions transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            // Update basic fields
            if (request.getApprovedBy() != null) {
                transaction.setApprovedBy(request.getApprovedBy());
            }

            // Set category-specific details
            setCategoryDetails(transaction, request);

            // Update items - ADD NEW or UPDATE EXISTING
            if (request.getItems() != null && !request.getItems().isEmpty()) {
                updateTransactionItems(transaction, request);
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

    // ==================== DELETE ====================
    @Override
    public ResponseEntity<String> deleteTransaction(Long id) {
        try {
            Transactions transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            // Return all items to stock
            for (TransactionsItems item : transaction.getTransactionItems()) {
                Goods goods = item.getGoods();
                goods.setQuantity(goods.getQuantity() + item.getQuantity());
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

    // ==================== GET BY ID ====================
    @Override
    public ResponseEntity<TransactionResponse> getTransactionById(Long id) {
        Transactions t = transactionRepository.findById(id).orElse(null);
        if (t == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapToResponse(t));
    }

    // ==================== GET ALL ====================
    @Override
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<Transactions> list = transactionRepository.findAll();
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : list) {
            responses.add(mapToResponse(t));
        }
        return ResponseEntity.ok(responses);
    }

    // ==================== FILTERS ====================
    @Override
    public ResponseEntity<List<TransactionResponse>> getByCategory(String category) {
        Transactions.TransactionCategory cat = Transactions.TransactionCategory.valueOf(category.toUpperCase());
        List<Transactions> list = transactionRepository.findByTransactionCategory(cat);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventId(Long eventId) {
        List<Transactions> list = transactionRepository.findByEventId(eventId);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventName(String eventName) {
        List<Transactions> list = transactionRepository.findByEvent_EventNameContainingIgnoreCase(eventName);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentId(Long departmentId) {
        List<Transactions> list = transactionRepository.findByDepartmentId(departmentId);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentName(String departmentName) {
        List<Transactions> list = transactionRepository.findByDepartment_DepartmentNameContainingIgnoreCase(departmentName);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverName(String name) {
        List<Transactions> list = transactionRepository.findByReceiverNameContainingIgnoreCase(name);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverContact(String contact) {
        List<Transactions> list = transactionRepository.findByReceiverContact(contact);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverName(String name) {
        List<Transactions> list = transactionRepository.findByEventReceiverNameContainingIgnoreCase(name);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverContact(String contact) {
        List<Transactions> list = transactionRepository.findByEventReceiverContact(contact);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssuedBy(Long userId) {
        List<Transactions> list = transactionRepository.findByIssuedById(userId);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceivedBy(Long userId) {
        List<Transactions> list = transactionRepository.findByReceivedById(userId);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByApprovedBy(String approvedBy) {
        List<Transactions> list = transactionRepository.findByApprovedByContainingIgnoreCase(approvedBy);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssueDateRange(LocalDateTime start, LocalDateTime end) {
        List<Transactions> list = transactionRepository.findByIssueDateBetween(start, end);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReturnDateRange(LocalDateTime start, LocalDateTime end) {
        List<Transactions> list = transactionRepository.findByReturnDateBetween(start, end);
        return ResponseEntity.ok(mapToResponseList(list));
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Set category-specific details (NORMAL or EVENT)
     */
    private void setCategoryDetails(Transactions transaction, TransactionRequest request) {
        if ("NORMAL".equalsIgnoreCase(request.getTransactionCategory())) {
            setNormalDetails(transaction, request);
        }
        if ("EVENT".equalsIgnoreCase(request.getTransactionCategory())) {
            setEventDetails(transaction, request);
        }
    }

    /**
     * Set EVENT transaction details
     */
    private void setEventDetails(Transactions transaction, TransactionRequest request) {
        if (request.getEventId() != null) {
            Events event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found with ID: " + request.getEventId()));
            transaction.setEvent(event);
        }
        if (request.getDepartmentId() != null) {
            Departments dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with ID: " + request.getDepartmentId()));
            transaction.setDepartment(dept);
        }
        if (request.getEventReceiverName() != null) {
            transaction.setEventReceiverName(request.getEventReceiverName());
        }
        if (request.getEventReceiverContact() != null) {
            transaction.setEventReceiverContact(request.getEventReceiverContact());
        }
    }

    /**
     * Set NORMAL transaction details
     */
    private void setNormalDetails(Transactions transaction, TransactionRequest request) {
        if (request.getReceiverName() != null) {
            transaction.setReceiverName(request.getReceiverName());
        }
        if (request.getReceiverContact() != null) {
            transaction.setReceiverContact(request.getReceiverContact());
        }
        if (request.getReceiverDutyPlace() != null) {
            transaction.setReceiverDutyPlace(request.getReceiverDutyPlace());
        }
    }

    /**
     * Update transaction items - add new or update existing quantities
     */
    private void updateTransactionItems(Transactions transaction, TransactionRequest request) {
        List<TransactionsItems> existingItems = transaction.getTransactionItems();

        for (TransactionRequest.ItemRequest itemReq : request.getItems()) {
            Goods goods = goodsRepository.findById(itemReq.getGoodsId())
                    .orElseThrow(() -> new RuntimeException("Goods not found: " + itemReq.getGoodsId()));

            // Find existing item
            TransactionsItems existingItem = existingItems.stream()
                    .filter(ei -> ei.getGoods().getId().equals(itemReq.getGoodsId()))
                    .findFirst().orElse(null);

            if (existingItem != null) {
                // UPDATE existing item quantity
                updateExistingItem(existingItem, goods, itemReq.getQuantity());
            } else {
                // ADD new item
                addNewItem(transaction, goods, itemReq.getQuantity(), existingItems);
            }
        }
    }

    /**
     * Update quantity of an existing transaction item
     */
    private void updateExistingItem(TransactionsItems item, Goods goods, int newQuantity) {
        int oldQuantity = item.getQuantity();
        int diff = newQuantity - oldQuantity;

        if (diff > 0) {
            // Increasing - check stock
            if (goods.getQuantity() < diff) {
                throw new RuntimeException("Insufficient quantity for: " + goods.getName() +
                        ". Available: " + goods.getQuantity() + ", Needed: " + diff);
            }
            goods.setQuantity(goods.getQuantity() - diff);
        } else if (diff < 0) {
            // Decreasing - return to stock
            goods.setQuantity(goods.getQuantity() + Math.abs(diff));
        }
        goodsRepository.save(goods);
        item.setQuantity(newQuantity);
        transactionItemRepository.save(item);
    }

    /**
     * Add a new item to transaction
     */
    private void addNewItem(Transactions transaction, Goods goods, int quantity, List<TransactionsItems> existingItems) {
        if (goods.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient quantity for: " + goods.getName());
        }

        goods.setQuantity(goods.getQuantity() - quantity);
        goodsRepository.save(goods);

        TransactionsItems newItem = new TransactionsItems();
        newItem.setTransactions(transaction);
        newItem.setGoods(goods);
        newItem.setQuantity(quantity);
        newItem.setQuantityReturned(0);
        newItem.setStatus(TransactionsItems.ItemStatus.ISSUED);
        transactionItemRepository.save(newItem);
        existingItems.add(newItem);
    }

    /**
     * Map entity list to response list
     */
    private List<TransactionResponse> mapToResponseList(List<Transactions> transactions) {
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : transactions) {
            responses.add(mapToResponse(t));
        }
        return responses;
    }

    /**
     * Map single transaction entity to response
     */
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

        // Map items
        List<TransactionResponse.ItemResponse> itemResponses = new ArrayList<>();
        if (t.getTransactionItems() != null) {
            for (TransactionsItems item : t.getTransactionItems()) {
                TransactionResponse.ItemResponse ir = new TransactionResponse.ItemResponse();
                ir.setId(item.getId());
                ir.setGoodsId(item.getGoods().getId());
                ir.setGoodsName(item.getGoods().getName());
                if (item.getGoods().getCategory() != null) {
                    ir.setUnit(item.getGoods().getCategory().getUnit());
                }
                ir.setQuantity(item.getQuantity());
                ir.setQuantityReturned(item.getQuantityReturned());
                ir.setStatus(item.getStatus().name());
                itemResponses.add(ir);
            }
        }
        response.setItems(itemResponses);

        return response;
    }
}