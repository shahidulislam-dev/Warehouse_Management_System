package com.warehouse_management.repositories;

import com.warehouse_management.entity.Warehouses;
import com.warehouse_management.wrapper.WarehouseWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouses, Long> {
    List<WarehouseWrapper> getAllWarehouses();
}


