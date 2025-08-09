package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.FloorController;
import com.warehouse_management.requests.FloorRequest;
import com.warehouse_management.responses.FloorResponse;
import com.warehouse_management.services.FloorService;
import com.warehouse_management.wrapper.FloorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FloorControllerImpl implements FloorController {

    private final FloorService floorService;

    @Autowired
    public FloorControllerImpl(FloorService floorService) {
        this.floorService = floorService;
    }

    @Override
    public ResponseEntity<String> create(FloorRequest request) {
        return floorService.createFloor(request);
    }

    @Override
    public ResponseEntity<List<FloorWrapper>> getAll() {
        return floorService.getAllFloors();
    }

    @Override
    public ResponseEntity<FloorResponse> getById(Long id) {
        return floorService.getFloorById(id);
    }

    @Override
    public ResponseEntity<List<FloorWrapper>> getByWarehouseId(Long warehouseId) {
        return floorService.getFloorsByWarehouseId(warehouseId);
    }

    @Override
    public ResponseEntity<String> update(Long id, FloorRequest request) {
        return floorService.updateFloor(id, request);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        return floorService.deleteFloor(id);
    }
}
