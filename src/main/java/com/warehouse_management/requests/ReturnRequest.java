package com.warehouse_management.requests;

import java.util.List;

public class ReturnRequest {

    private Long transactionId;
    private List<ReturnItem> items;

    public static class ReturnItem {
        private Long transactionItemId;
        private Integer quantityReturned;

        public Long getTransactionItemId() { return transactionItemId; }
        public void setTransactionItemId(Long transactionItemId) { this.transactionItemId = transactionItemId; }
        public Integer getQuantityReturned() { return quantityReturned; }
        public void setQuantityReturned(Integer quantityReturned) { this.quantityReturned = quantityReturned; }
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public List<ReturnItem> getItems() { return items; }
    public void setItems(List<ReturnItem> items) { this.items = items; }
}