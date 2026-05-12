package com.warehouse_management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unused")
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

    @ManyToOne(fetch = FetchType.EAGER)
    private User issuedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private User receivedBy;

    private LocalDateTime issueDate;
    private LocalDateTime returnDate;

    private String approvedBy;
    private String returnNotes;

    // NORMAL TRANSACTION FIELDS
    private String receiverName;
    private String receiverContact;
    private String receiverDutyPlace;

    // EVENT TRANSACTION FIELDS
    @ManyToOne(fetch = FetchType.EAGER)
    private Events event;

    @ManyToOne(fetch = FetchType.EAGER)
    private Departments department;

    private String eventReceiverName;
    private String eventReceiverContact;

    @OneToMany(mappedBy = "transactions", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionsItems> transactionItems = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum TransactionCategory {
        EVENT, NORMAL
    }

    public enum TransactionStatus {
        ISSUED,
        PARTIALLY_RETURNED,
        RETURNED,
        CANCELLED
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
    public User getReceivedBy() { return receivedBy; }
    public void setReceivedBy(User receivedBy) { this.receivedBy = receivedBy; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getReturnNotes() { return returnNotes; }
    public void setReturnNotes(String returnNotes) { this.returnNotes = returnNotes; }
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