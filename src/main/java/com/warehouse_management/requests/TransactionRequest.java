package com.warehouse_management.requests;

import java.util.List;
@SuppressWarnings("unused")
public class TransactionRequest {
    private String transactionCategory;
    private String approvedBy;
    private List<ItemRequest> items;

    // Normal fields
    private String receiverName;
    private String receiverContact;
    private String receiverDutyPlace;

    // Event fields
    private Long eventId;
    private Long departmentId;
    private String eventReceiverName;
    private String eventReceiverContact;

    public static class ItemRequest {
        private Long goodsId;
        private Integer quantity;
        private String returnableType; // RETURNABLE or NON_RETURNABLE

        public Long getGoodsId() { return goodsId; }
        public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getReturnableType() { return returnableType; }
        public void setReturnableType(String returnableType) { this.returnableType = returnableType; }
    }

    // Getters and Setters
    public String getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(String transactionCategory) { this.transactionCategory = transactionCategory; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverContact() { return receiverContact; }
    public void setReceiverContact(String receiverContact) { this.receiverContact = receiverContact; }
    public String getReceiverDutyPlace() { return receiverDutyPlace; }
    public void setReceiverDutyPlace(String receiverDutyPlace) { this.receiverDutyPlace = receiverDutyPlace; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getEventReceiverName() { return eventReceiverName; }
    public void setEventReceiverName(String eventReceiverName) { this.eventReceiverName = eventReceiverName; }
    public String getEventReceiverContact() { return eventReceiverContact; }
    public void setEventReceiverContact(String eventReceiverContact) { this.eventReceiverContact = eventReceiverContact; }
}