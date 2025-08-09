package com.warehouse_management.services;

import com.warehouse_management.requests.FloorRequest;
import com.warehouse_management.responses.FloorResponse;
import com.warehouse_management.wrapper.FloorWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FloorService {
    ResponseEntity<String> createFloor(FloorRequest request);
    ResponseEntity<List<FloorWrapper>> getAllFloors();
    ResponseEntity<FloorResponse> getFloorById(Long id);
    ResponseEntity<List<FloorWrapper>> getFloorsByWarehouseId(Long warehouseId);
    ResponseEntity<String> updateFloor(Long id, FloorRequest request);
    ResponseEntity<String> deleteFloor(Long id);
}
