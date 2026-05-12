package com.warehouse_management.requests;

import java.util.List;
@SuppressWarnings("unused")
public class ReturnRequest {
    private Long transactionId;
    private String returnNotes;  // ADD THIS
    private List<ReturnItem> items;

    public static class ReturnItem {
        private Long transactionItemId;
        private Integer quantityReturned;
        private Integer quantityDamaged;
        private Integer quantityLost;
        private String status;
        private String notes;

        public Long getTransactionItemId() { return transactionItemId; }
        public void setTransactionItemId(Long transactionItemId) { this.transactionItemId = transactionItemId; }
        public Integer getQuantityReturned() { return quantityReturned; }
        public void setQuantityReturned(Integer quantityReturned) { this.quantityReturned = quantityReturned; }
        public Integer getQuantityDamaged() { return quantityDamaged; }
        public void setQuantityDamaged(Integer quantityDamaged) { this.quantityDamaged = quantityDamaged; }
        public Integer getQuantityLost() { return quantityLost; }
        public void setQuantityLost(Integer quantityLost) { this.quantityLost = quantityLost; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getReturnNotes() { return returnNotes; }  // ADD THIS
    public void setReturnNotes(String returnNotes) { this.returnNotes = returnNotes; }  // ADD THIS
    public List<ReturnItem> getItems() { return items; }
    public void setItems(List<ReturnItem> items) { this.items = items; }
}