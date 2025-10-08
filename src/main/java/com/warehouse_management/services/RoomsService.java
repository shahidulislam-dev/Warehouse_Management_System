package com.warehouse_management.services;


import com.warehouse_management.requests.RoomRequest;
import com.warehouse_management.responses.RoomResponse;
import com.warehouse_management.wrapper.RoomsWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomsService {
    ResponseEntity<String> createRoom(RoomRequest request);
    ResponseEntity<List<RoomsWrapper>> getAllRooms();
    ResponseEntity<RoomResponse> getRoomById(Long id);
    ResponseEntity<List<RoomsWrapper>> getRoomsByFloorAndWarehouse(Long floorId, Long warehouseId);
    ResponseEntity<List<RoomsWrapper>> getRoomsByWarehouseId(Long warehouseId);

    ResponseEntity<String> updateRoom(Long id, RoomRequest request);
    ResponseEntity<String> deleteRoom(Long id);
}
