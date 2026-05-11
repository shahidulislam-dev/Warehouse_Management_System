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

            setCategoryDetails(transaction, request);

            List<TransactionsItems> items = new ArrayList<>();
            for (TransactionRequest.ItemRequest itemReq : request.getItems()) {
                Goods goods = goodsRepository.findById(itemReq.getGoodsId())
                        .orElseThrow(() -> new RuntimeException("Goods not found: " + itemReq.getGoodsId()));

                try {
                    reduceStock(goods, itemReq.getQuantity(), goods.getName());
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }

                TransactionsItems item = createTransactionItem(transaction, goods, itemReq);
                items.add(item);
            }
            transaction.setTransactionItems(items);

            updateTransactionStatus(transaction);
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

            if (transaction.getStatus() == Transactions.TransactionStatus.CANCELLED) {
                return ResponseEntity.badRequest().body("Transaction is cancelled");
            }

            String currentUser = jwtFilter.getCurrentUser();
            User receivedBy = userRepository.findByEmailId(currentUser);
            if (receivedBy == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            for (ReturnRequest.ReturnItem returnItem : request.getItems()) {
                TransactionsItems item = transactionItemRepository.findById(returnItem.getTransactionItemId())
                        .orElseThrow(() -> new RuntimeException("Item not found: " + returnItem.getTransactionItemId()));

                int returned = returnItem.getQuantityReturned() != null ? returnItem.getQuantityReturned() : 0;
                int damaged = returnItem.getQuantityDamaged() != null ? returnItem.getQuantityDamaged() : 0;
                int lost = returnItem.getQuantityLost() != null ? returnItem.getQuantityLost() : 0;

                if (returned == 0 && damaged == 0 && lost == 0) continue;

                int currentReturned = item.getQuantityReturned() != null ? item.getQuantityReturned() : 0;
                int currentLost = item.getQuantityLost() != null ? item.getQuantityLost() : 0;
                int currentDamaged = item.getQuantityDamaged() != null ? item.getQuantityDamaged() : 0;

                int alreadyResolved = currentReturned + currentLost;
                int remaining = item.getQuantity() - alreadyResolved;
                int newTotal = returned + damaged + lost;

                if (newTotal > remaining) {
                    return ResponseEntity.badRequest().body(
                            "Cannot process more than remaining for: " + item.getGoods().getName() +
                                    ". Issued: " + item.getQuantity() +
                                    ", Already resolved: " + alreadyResolved +
                                    ", Remaining: " + remaining +
                                    ", Attempted: " + newTotal
                    );
                }

                // Process stock - only for returnable items
                if (item.getReturnableType() != TransactionsItems.ReturnableType.NON_RETURNABLE) {
                    int totalToStock = returned + damaged;
                    if (totalToStock > 0) {
                        addStock(item.getGoods(), totalToStock);
                    }
                }

                // Update quantities
                item.setQuantityReturned(currentReturned + returned + damaged);
                if (damaged > 0) item.setQuantityDamaged(currentDamaged + damaged);
                if (lost > 0) item.setQuantityLost(currentLost + lost);

                item.setStatus(determineItemStatus(item, null));
                if (returnItem.getNotes() != null && !returnItem.getNotes().trim().isEmpty()) {
                    item.setNotes(returnItem.getNotes());
                }
                transactionItemRepository.save(item);
            }

            updateTransactionStatus(transaction);
            transaction.setReceivedBy(receivedBy);
            transaction.setReturnDate(LocalDateTime.now());
            if (request.getReturnNotes() != null) {
                transaction.setReturnNotes(request.getReturnNotes());
            }
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Transaction processed successfully");

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

            if (transaction.getStatus() == Transactions.TransactionStatus.CANCELLED) {
                return ResponseEntity.badRequest().body("Cannot update cancelled transaction");
            }

            if (request.getApprovedBy() != null) {
                transaction.setApprovedBy(request.getApprovedBy());
            }

            setCategoryDetails(transaction, request);

            if (request.getItems() != null && !request.getItems().isEmpty()) {
                updateTransactionItems(transaction, request);
            }

            updateTransactionStatus(transaction);
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

            for (TransactionsItems item : transaction.getTransactionItems()) {
                if (item.getReturnableType() == TransactionsItems.ReturnableType.NON_RETURNABLE) continue;

                int returned = item.getQuantityReturned() != null ? item.getQuantityReturned() : 0;
                int lost = item.getQuantityLost() != null ? item.getQuantityLost() : 0;
                int toReturn = item.getQuantity() - lost - returned;

                if (toReturn > 0) addStock(item.getGoods(), toReturn);
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

    // ==================== GET METHODS ====================
    @Override
    public ResponseEntity<TransactionResponse> getTransactionById(Long id) {
        Transactions t = transactionRepository.findById(id).orElse(null);
        if (t == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapToResponse(t));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findAll()));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByCategory(String category) {
        Transactions.TransactionCategory cat = Transactions.TransactionCategory.valueOf(category.toUpperCase());
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByTransactionCategory(cat)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventId(Long eventId) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByEventId(eventId)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventName(String eventName) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByEvent_EventNameContainingIgnoreCase(eventName)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentId(Long departmentId) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByDepartmentId(departmentId)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByDepartmentName(String departmentName) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByDepartment_DepartmentNameContainingIgnoreCase(departmentName)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverName(String name) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByReceiverNameContainingIgnoreCase(name)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceiverContact(String contact) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByReceiverContact(contact)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverName(String name) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByEventReceiverNameContainingIgnoreCase(name)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByEventReceiverContact(String contact) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByEventReceiverContact(contact)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssuedBy(Long userId) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByIssuedById(userId)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReceivedBy(Long userId) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByReceivedById(userId)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByApprovedBy(String approvedBy) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByApprovedByContainingIgnoreCase(approvedBy)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByIssueDateRange(LocalDateTime start, LocalDateTime end) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByIssueDateBetween(start, end)));
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getByReturnDateRange(LocalDateTime start, LocalDateTime end) {
        return ResponseEntity.ok(mapToResponseList(transactionRepository.findByReturnDateBetween(start, end)));
    }

    // ==================== STOCK HELPERS ====================
    private void reduceStock(Goods goods, int quantity, String goodsName) {
        if (goods.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient quantity for: " + goodsName +
                    ". Available: " + goods.getQuantity() + ", Requested: " + quantity);
        }
        goods.setQuantity(goods.getQuantity() - quantity);
        goodsRepository.save(goods);
    }

    private void addStock(Goods goods, int quantity) {
        goods.setQuantity(goods.getQuantity() + quantity);
        goodsRepository.save(goods);
    }

    // ==================== CATEGORY HELPER ====================
    private void setCategoryDetails(Transactions transaction, TransactionRequest request) {
        if ("NORMAL".equalsIgnoreCase(request.getTransactionCategory())) {
            if (request.getReceiverName() != null) transaction.setReceiverName(request.getReceiverName());
            if (request.getReceiverContact() != null) transaction.setReceiverContact(request.getReceiverContact());
            if (request.getReceiverDutyPlace() != null) transaction.setReceiverDutyPlace(request.getReceiverDutyPlace());
        }
        if ("EVENT".equalsIgnoreCase(request.getTransactionCategory())) {
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
            if (request.getEventReceiverName() != null) transaction.setEventReceiverName(request.getEventReceiverName());
            if (request.getEventReceiverContact() != null) transaction.setEventReceiverContact(request.getEventReceiverContact());
        }
    }

    // ==================== ITEM HELPERS ====================
    private TransactionsItems createTransactionItem(Transactions transaction, Goods goods, TransactionRequest.ItemRequest itemReq) {
        TransactionsItems item = new TransactionsItems();
        item.setTransactions(transaction);
        item.setGoods(goods);
        item.setQuantity(itemReq.getQuantity());
        item.setQuantityReturned(0);
        item.setQuantityDamaged(0);
        item.setQuantityLost(0);

        if (itemReq.getReturnableType() != null) {
            item.setReturnableType(TransactionsItems.ReturnableType.valueOf(itemReq.getReturnableType()));
        } else {
            item.setReturnableType(TransactionsItems.ReturnableType.RETURNABLE);
        }

        // ✅ NON-RETURNABLE: Immediately marked as CONSUMED
        if (item.getReturnableType() == TransactionsItems.ReturnableType.NON_RETURNABLE) {
            item.setQuantityReturned(itemReq.getQuantity());
            item.setStatus(TransactionsItems.ItemStatus.CONSUMED);
        } else {
            item.setStatus(TransactionsItems.ItemStatus.ISSUED);
        }

        return item;
    }

    private void updateTransactionItems(Transactions transaction, TransactionRequest request) {
        List<TransactionsItems> existingItems = transaction.getTransactionItems();

        for (TransactionRequest.ItemRequest itemReq : request.getItems()) {
            Goods goods = goodsRepository.findById(itemReq.getGoodsId())
                    .orElseThrow(() -> new RuntimeException("Goods not found: " + itemReq.getGoodsId()));

            TransactionsItems existingItem = null;
            for (TransactionsItems ei : existingItems) {
                if (ei.getGoods().getId().equals(itemReq.getGoodsId())) {
                    existingItem = ei;
                    break;
                }
            }

            if (existingItem != null) {
                int oldQuantity = existingItem.getQuantity();
                int newQuantity = itemReq.getQuantity();
                int diff = newQuantity - oldQuantity;

                if (diff > 0) {
                    // Increasing - take more from stock
                    reduceStock(goods, diff, goods.getName());

                    // PRESERVE previous return/damage/lost - only cap if they exceed new total
                    if (existingItem.getQuantityReturned() != null && existingItem.getQuantityReturned() > newQuantity) {
                        existingItem.setQuantityReturned(newQuantity);
                    }
                    if (existingItem.getQuantityDamaged() != null && existingItem.getQuantityDamaged() > newQuantity) {
                        existingItem.setQuantityDamaged(newQuantity);
                    }
                    if (existingItem.getQuantityLost() != null && existingItem.getQuantityLost() > newQuantity) {
                        existingItem.setQuantityLost(newQuantity);
                    }

                } else if (diff < 0) {
                    // Decreasing - return to stock
                    addStock(goods, Math.abs(diff));
                }

                existingItem.setQuantity(newQuantity);
                existingItem.setStatus(determineItemStatus(existingItem, null));
                transactionItemRepository.save(existingItem);

            } else {
                // ADD new item
                reduceStock(goods, itemReq.getQuantity(), goods.getName());
                TransactionsItems newItem = createTransactionItem(transaction, goods, itemReq);
                transactionItemRepository.save(newItem);
                existingItems.add(newItem);
            }
        }
    }

    // ==================== STATUS HELPERS ====================
    private TransactionsItems.ItemStatus determineItemStatus(TransactionsItems item, String requestedStatus) {
        if (requestedStatus != null) {
            try {
                return TransactionsItems.ItemStatus.valueOf(requestedStatus);
            } catch (IllegalArgumentException e) {}
        }

        int totalReturned = item.getQuantityReturned() != null ? item.getQuantityReturned() : 0;
        int totalLost = item.getQuantityLost() != null ? item.getQuantityLost() : 0;
        int totalDamaged = item.getQuantityDamaged() != null ? item.getQuantityDamaged() : 0;
        int totalResolved = totalReturned + totalLost;

        if (item.getReturnableType() == TransactionsItems.ReturnableType.NON_RETURNABLE) {
            if (totalResolved >= item.getQuantity()) return TransactionsItems.ItemStatus.CONSUMED;
            else if (totalResolved > 0) return TransactionsItems.ItemStatus.PARTIALLY_CONSUMED;
            return TransactionsItems.ItemStatus.ISSUED;
        }

        if (totalResolved <= 0) return TransactionsItems.ItemStatus.ISSUED;
        else if (totalResolved >= item.getQuantity()) {
            if (totalDamaged > 0) return TransactionsItems.ItemStatus.DAMAGED;
            return TransactionsItems.ItemStatus.RETURNED;
        }
        return TransactionsItems.ItemStatus.PARTIALLY_RETURNED;
    }

    private void updateTransactionStatus(Transactions transaction) {
        List<TransactionsItems> items = transaction.getTransactionItems();
        boolean allResolved = true;

        for (TransactionsItems item : items) {
            TransactionsItems.ItemStatus status = item.getStatus();
            if (status == TransactionsItems.ItemStatus.ISSUED ||
                    status == TransactionsItems.ItemStatus.PARTIALLY_RETURNED ||
                    status == TransactionsItems.ItemStatus.PARTIALLY_CONSUMED) {
                allResolved = false;
                break;
            }
        }

        if (allResolved) {
            transaction.setStatus(Transactions.TransactionStatus.RETURNED);
        } else {
            boolean anyResolved = items.stream().anyMatch(i ->
                    i.getStatus() != TransactionsItems.ItemStatus.ISSUED);
            if (anyResolved) {
                transaction.setStatus(Transactions.TransactionStatus.PARTIALLY_RETURNED);
            } else {
                transaction.setStatus(Transactions.TransactionStatus.ISSUED);
            }
        }
    }

    // ==================== MAPPING HELPERS ====================
    private List<TransactionResponse> mapToResponseList(List<Transactions> transactions) {
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transactions t : transactions) responses.add(mapToResponse(t));
        return responses;
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
        response.setIssueDate(t.getIssueDate());
        response.setReturnDate(t.getReturnDate());
        response.setReturnNotes(t.getReturnNotes());
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

        List<TransactionResponse.ItemResponse> itemResponses = new ArrayList<>();
        if (t.getTransactionItems() != null) {
            for (TransactionsItems item : t.getTransactionItems()) {
                TransactionResponse.ItemResponse ir = new TransactionResponse.ItemResponse();
                ir.setId(item.getId());
                ir.setGoodsId(item.getGoods().getId());
                ir.setGoodsName(item.getGoods().getName());
                if (item.getGoods().getCategory() != null) ir.setUnit(item.getGoods().getCategory().getUnit());
                ir.setQuantity(item.getQuantity());
                ir.setQuantityReturned(item.getQuantityReturned());
                ir.setQuantityDamaged(item.getQuantityDamaged());
                ir.setQuantityLost(item.getQuantityLost());
                ir.setStatus(item.getStatus().name());
                ir.setReturnableType(item.getReturnableType() != null ? item.getReturnableType().name() : null);
                ir.setNotes(item.getNotes());
                itemResponses.add(ir);
            }
        }
        response.setItems(itemResponses);
        return response;
    }
}