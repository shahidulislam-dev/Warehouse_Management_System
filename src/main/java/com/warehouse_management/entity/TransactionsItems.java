package com.warehouse_management.entity;

import jakarta.persistence.*;

@Entity
public class TransactionsItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id")
    private Transactions transactions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    private Integer quantity;
    private Integer quantityReturned;
    private Integer quantityDamaged;
    private Integer quantityLost;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Enumerated(EnumType.STRING)
    private ReturnableType returnableType; // RETURNABLE or NON_RETURNABLE

    private String notes; // Notes about damage/loss

    public enum ItemStatus {
        ISSUED,
        PARTIALLY_RETURNED,
        RETURNED,
        DAMAGED,
        LOST,
        CONSUMED,
        PARTIALLY_CONSUMED
    }

    public enum ReturnableType {
        RETURNABLE,
        NON_RETURNABLE
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Transactions getTransactions() { return transactions; }
    public void setTransactions(Transactions transactions) { this.transactions = transactions; }
    public Goods getGoods() { return goods; }
    public void setGoods(Goods goods) { this.goods = goods; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getQuantityReturned() { return quantityReturned; }
    public void setQuantityReturned(Integer quantityReturned) { this.quantityReturned = quantityReturned; }
    public Integer getQuantityDamaged() { return quantityDamaged; }
    public void setQuantityDamaged(Integer quantityDamaged) { this.quantityDamaged = quantityDamaged; }
    public Integer getQuantityLost() { return quantityLost; }
    public void setQuantityLost(Integer quantityLost) { this.quantityLost = quantityLost; }
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
    public ReturnableType getReturnableType() { return returnableType; }
    public void setReturnableType(ReturnableType returnableType) { this.returnableType = returnableType; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}