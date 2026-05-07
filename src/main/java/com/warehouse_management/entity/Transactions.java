package com.warehouse_management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionCategory transactionCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    // Issued By
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issued_by")
    private User issuedBy;

    private LocalDateTime issueDate;

    private String approvedBy;

    // Return Info
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    private LocalDateTime returnDate;

    // ===== NORMAL TRANSACTION FIELDS =====
    private String receiverName;
    private String receiverContact;
    private String receiverDutyPlace;

    // ===== EVENT TRANSACTION FIELDS =====
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Events event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Departments department;

    private String eventReceiverName;
    private String eventReceiverContact;

    // ===== MULTIPLE ITEMS =====
    @OneToMany(mappedBy = "transactions", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionsItems> transactionItems = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum TransactionCategory {
        EVENT, NORMAL
    }

    public enum TransactionStatus {
        ISSUED, RETURNED, CANCELLED
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TransactionCategory getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(TransactionCategory transactionCategory) { this.transactionCategory = transactionCategory; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public User getIssuedBy() { return issuedBy; }
    public void setIssuedBy(User issuedBy) { this.issuedBy = issuedBy; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public User getReceivedBy() { return receivedBy; }
    public void setReceivedBy(User receivedBy) { this.receivedBy = receivedBy; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverContact() { return receiverContact; }
    public void setReceiverContact(String receiverContact) { this.receiverContact = receiverContact; }
    public String getReceiverDutyPlace() { return receiverDutyPlace; }
    public void setReceiverDutyPlace(String receiverDutyPlace) { this.receiverDutyPlace = receiverDutyPlace; }
    public Events getEvent() { return event; }
    public void setEvent(Events event) { this.event = event; }
    public Departments getDepartment() { return department; }
    public void setDepartment(Departments department) { this.department = department; }
    public String getEventReceiverName() { return eventReceiverName; }
    public void setEventReceiverName(String eventReceiverName) { this.eventReceiverName = eventReceiverName; }
    public String getEventReceiverContact() { return eventReceiverContact; }
    public void setEventReceiverContact(String eventReceiverContact) { this.eventReceiverContact = eventReceiverContact; }
    public List<TransactionsItems> getTransactionItems() { return transactionItems; }
    public void setTransactionItems(List<TransactionsItems> transactionItems) { this.transactionItems = transactionItems; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}