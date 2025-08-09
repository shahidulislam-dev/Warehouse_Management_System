package com.warehouse_management.repositories;

import com.warehouse_management.entity.User;
import com.warehouse_management.entity.Warehouses;
import com.warehouse_management.wrapper.WarehouseWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouses, Long> {
    List<WarehouseWrapper> getAllWarehouses();
}


