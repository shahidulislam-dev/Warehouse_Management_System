package com.warehouse_management.responses;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionResponse {

    private Long id;
    private String transactionCategory;
    private String status;
    private Long issuedById;
    private String issuedByName;
    private Long receivedById;
    private String receivedByName;
    private String approvedBy;
    private LocalDateTime issueDate;
    private LocalDateTime returnDate;
    private String returnNotes;

    // Normal fields
    private String receiverName;
    private String receiverContact;
    private String receiverDutyPlace;

    // Event fields
    private Long eventId;
    private String eventName;
    private Long departmentId;
    private String departmentName;
    private String eventReceiverName;
    private String eventReceiverContact;

    // Multiple items
    private List<ItemResponse> items;

    private LocalDateTime createdAt;

    // ==================== INNER CLASS ====================
    public static class ItemResponse {
        private Long id;
        private Long goodsId;
        private String goodsName;
        private String unit;
        private Integer quantity;
        private Integer quantityReturned;
        private Integer quantityDamaged;
        private Integer quantityLost;
        private String status;
        private String returnableType;
        private String notes;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getGoodsId() { return goodsId; }
        public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }
        public String getGoodsName() { return goodsName; }
        public void setGoodsName(String goodsName) { this.goodsName = goodsName; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Integer getQuantityReturned() { return quantityReturned; }
        public void setQuantityReturned(Integer quantityReturned) { this.quantityReturned = quantityReturned; }
        public Integer getQuantityDamaged() { return quantityDamaged; }
        public void setQuantityDamaged(Integer quantityDamaged) { this.quantityDamaged = quantityDamaged; }
        public Integer getQuantityLost() { return quantityLost; }
        public void setQuantityLost(Integer quantityLost) { this.quantityLost = quantityLost; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getReturnableType() { return returnableType; }
        public void setReturnableType(String returnableType) { this.returnableType = returnableType; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    // ==================== GETTERS AND SETTERS ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(String transactionCategory) { this.transactionCategory = transactionCategory; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getIssuedById() { return issuedById; }
    public void setIssuedById(Long issuedById) { this.issuedById = issuedById; }
    public String getIssuedByName() { return issuedByName; }
    public void setIssuedByName(String issuedByName) { this.issuedByName = issuedByName; }
    public Long getReceivedById() { return receivedById; }
    public void setReceivedById(Long receivedById) { this.receivedById = receivedById; }
    public String getReceivedByName() { return receivedByName; }
    public void setReceivedByName(String receivedByName) { this.receivedByName = receivedByName; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public String getReturnNotes() { return returnNotes; }
    public void setReturnNotes(String returnNotes) { this.returnNotes = returnNotes; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverContact() { return receiverContact; }
    public void setReceiverContact(String receiverContact) { this.receiverContact = receiverContact; }
    public String getReceiverDutyPlace() { return receiverDutyPlace; }
    public void setReceiverDutyPlace(String receiverDutyPlace) { this.receiverDutyPlace = receiverDutyPlace; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getEventReceiverName() { return eventReceiverName; }
    public void setEventReceiverName(String eventReceiverName) { this.eventReceiverName = eventReceiverName; }
    public String getEventReceiverContact() { return eventReceiverContact; }
    public void setEventReceiverContact(String eventReceiverContact) { this.eventReceiverContact = eventReceiverContact; }
    public List<ItemResponse> getItems() { return items; }
    public void setItems(List<ItemResponse> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}