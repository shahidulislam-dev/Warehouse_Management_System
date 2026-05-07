package com.warehouse_management.repositories;

import com.warehouse_management.entity.TransactionsItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionsItems, Long> {
    List<TransactionsItems> findByTransactionsId(Long transactionId);
    List<TransactionsItems> findByGoodsId(Long goodsId);
}