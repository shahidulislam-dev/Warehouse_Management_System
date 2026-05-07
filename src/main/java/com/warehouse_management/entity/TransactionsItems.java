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

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public enum ItemStatus {
        ISSUED, RETURNED, DAMAGED, LOST
    }

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
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
}