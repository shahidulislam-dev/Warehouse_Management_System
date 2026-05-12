package com.warehouse_management.repositories;

import com.warehouse_management.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    // By category
    List<Transactions> findByTransactionCategory(Transactions.TransactionCategory category);

    // By event
    List<Transactions> findByEventId(Long eventId);
    List<Transactions> findByEvent_EventNameContainingIgnoreCase(String eventName);

    // By department
    List<Transactions> findByDepartmentId(Long departmentId);
    List<Transactions> findByDepartment_DepartmentNameContainingIgnoreCase(String departmentName);

    // By receiver (NORMAL)
    List<Transactions> findByReceiverNameContainingIgnoreCase(String receiverName);
    List<Transactions> findByReceiverContact(String receiverContact);

    // By receiver (EVENT)
    List<Transactions> findByEventReceiverNameContainingIgnoreCase(String name);
    List<Transactions> findByEventReceiverContact(String contact);

    // By users
    List<Transactions> findByIssuedById(Long userId);
    List<Transactions> findByReceivedById(Long userId);

    // By approver
    List<Transactions> findByApprovedByContainingIgnoreCase(String approvedBy);

    // By dates
    List<Transactions> findByIssueDateBetween(LocalDateTime start, LocalDateTime end);
    List<Transactions> findByReturnDateBetween(LocalDateTime start, LocalDateTime end);

    // By status
//    List<Transactions> findByStatus(Transactions.TransactionStatus status);
}