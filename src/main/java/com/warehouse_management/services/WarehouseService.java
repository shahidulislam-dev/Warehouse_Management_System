package com.warehouse_management.services;

import com.warehouse_management.requests.WarehouseRequest;
import com.warehouse_management.responses.WarehouseResponse;
import com.warehouse_management.wrapper.WarehouseWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WarehouseService {
    ResponseEntity<String> createWarehouse(WarehouseRequest request);
    ResponseEntity<List<WarehouseWrapper>> getAllWarehouses();
    ResponseEntity<WarehouseResponse> getWarehouseById(Long id);
    ResponseEntity<String> updateWarehouse(Long id, WarehouseRequest request);
    ResponseEntity<String> deleteWarehouse(Long id);
}
