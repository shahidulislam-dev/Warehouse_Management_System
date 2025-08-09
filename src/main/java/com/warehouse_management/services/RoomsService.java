package com.warehouse_management.services;


import com.warehouse_management.requests.RoomRequest;
import com.warehouse_management.responses.RoomResponse;
import com.warehouse_management.wrapper.RoomsWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomsService {
    ResponseEntity<String> createFloor(RoomRequest request);
    ResponseEntity<List<RoomsWrapper>> getAllFloors();
    ResponseEntity<RoomResponse> getFloorById(Long id);
    ResponseEntity<List<RoomsWrapper>> getRoomsByFloorAndWarehouse(Long floorId, Long warehouseId);
    ResponseEntity<List<RoomsWrapper>> getRoomsByWarehouseId(Long warehouseId);

    ResponseEntity<String> updateFloor(Long id, RoomRequest request);
    ResponseEntity<String> deleteFloor(Long id);
}
