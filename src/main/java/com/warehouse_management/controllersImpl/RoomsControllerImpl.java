package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.RoomsController;
import com.warehouse_management.requests.RoomRequest;
import com.warehouse_management.responses.RoomResponse;
import com.warehouse_management.services.RoomsService;
import com.warehouse_management.wrapper.RoomsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomsControllerImpl implements RoomsController {

    private final RoomsService roomsService;

    @Autowired
    public RoomsControllerImpl(RoomsService roomsService) {
        this.roomsService = roomsService;
    }

    @Override
    public ResponseEntity<String> create(RoomRequest request) {
        return roomsService.createFloor(request);
    }

    @Override
    public ResponseEntity<List<RoomsWrapper>> getAll() {
        return roomsService.getAllFloors();
    }

    @Override
    public ResponseEntity<RoomResponse> getById(Long id) {
        return roomsService.getFloorById(id);
    }

    @Override
    public ResponseEntity<List<RoomsWrapper>> getByWarehouseId(Long floorId, Long warehouseId) {
        return roomsService.getRoomsByFloorAndWarehouse(floorId, warehouseId);
    }
    @Override
    public ResponseEntity<List<RoomsWrapper>> getRoomsByWarehouse(Long warehouseId) {
        return roomsService.getRoomsByWarehouseId(warehouseId);
    }
    @Override
    public ResponseEntity<String> update(Long id, RoomRequest request) {
        return roomsService.updateFloor(id, request);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        ResponseEntity<String> response = roomsService.deleteFloor(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }
}
