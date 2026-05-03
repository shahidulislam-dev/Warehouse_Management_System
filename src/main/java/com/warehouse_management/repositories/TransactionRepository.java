package com.warehouse_management.repositories;

import com.warehouse_management.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findByTransactionCategory(Transactions.TransactionCategory category);

    List<Transactions> findByEventId(Long eventId);

    List<Transactions> findByEvent_EventNameContainingIgnoreCase(String eventName);

    List<Transactions> findByDepartmentId(Long departmentId);

    List<Transactions> findByDepartment_DepartmentNameContainingIgnoreCase(String departmentName);

    List<Transactions> findByReceiverNameContainingIgnoreCase(String receiverName);

    List<Transactions> findByReceiverContact(String receiverContact);

    List<Transactions> findByEventReceiverNameContainingIgnoreCase(String name);

    List<Transactions> findByEventReceiverContact(String contact);

    List<Transactions> findByIssuedById(Long userId);

    List<Transactions> findByReceivedById(Long userId);

    List<Transactions> findByApprovedByContainingIgnoreCase(String approvedBy);

    List<Transactions> findByIssueDateBetween(LocalDateTime start, LocalDateTime end);

    List<Transactions> findByReturnDateBetween(LocalDateTime start, LocalDateTime end);

    List<Transactions> findByStatus(Transactions.TransactionStatus status);

    List<Transactions> findByGoodsId(Long goodsId);
}