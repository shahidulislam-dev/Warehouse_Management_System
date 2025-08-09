package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.WarehouseController;
import com.warehouse_management.requests.WarehouseRequest;
import com.warehouse_management.responses.WarehouseResponse;
import com.warehouse_management.services.WarehouseService;
import com.warehouse_management.wrapper.WarehouseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WarehouseControllerImpl implements WarehouseController {
    private final WarehouseService warehouseService;
    @Autowired
    public WarehouseControllerImpl(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Override
    public ResponseEntity<WarehouseResponse> create(WarehouseRequest request) {
        ResponseEntity<String> response = warehouseService.createWarehouse(request);
        return new ResponseEntity<>(new WarehouseResponse(null, response.getBody()), response.getStatusCode());
    }

    @Override
    public ResponseEntity<List<WarehouseResponse>> getAll() {
        ResponseEntity<List<WarehouseWrapper>> wrapperResponse = warehouseService.getAllWarehouses();
        List<WarehouseResponse> converted = wrapperResponse.getBody().stream()
                .map(w -> new WarehouseResponse(w.getId(), w.getName()))
                .toList();
        return new ResponseEntity<>(converted, wrapperResponse.getStatusCode());
    }

    @Override
    public ResponseEntity<WarehouseResponse> getById(Long id) {
        return warehouseService.getWarehouseById(id);
    }

    @Override
    public ResponseEntity<WarehouseResponse> update(Long id, WarehouseRequest request) {
        ResponseEntity<String> response = warehouseService.updateWarehouse(id, request);
        return new ResponseEntity<>(new WarehouseResponse(id, response.getBody()), response.getStatusCode());
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        ResponseEntity<String> response = warehouseService.deleteWarehouse(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }
}
