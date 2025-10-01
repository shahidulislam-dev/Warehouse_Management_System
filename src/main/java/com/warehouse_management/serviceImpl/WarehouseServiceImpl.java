package com.warehouse_management.serviceImpl;

import com.warehouse_management.constants.WarehouseConstant;
import com.warehouse_management.entity.Warehouses;
import com.warehouse_management.jwt.JwtFilter;
import com.warehouse_management.repositories.WarehouseRepository;
import com.warehouse_management.requests.WarehouseRequest;
import com.warehouse_management.responses.WarehouseResponse;
import com.warehouse_management.services.WarehouseService;
import com.warehouse_management.utils.WarehouseUtils;
import com.warehouse_management.wrapper.WarehouseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehousesRepository;
    private final JwtFilter jwtFilter;
    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehousesRepository,JwtFilter jwtFilter) {
        this.warehousesRepository = warehousesRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> createWarehouse(WarehouseRequest request) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Warehouses warehouse = new Warehouses();
            warehouse.setName(request.getName());
            warehousesRepository.save(warehouse);
            return WarehouseUtils.getResponseEntity("Warehouse created successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<WarehouseWrapper>> getAllWarehouses() {
        try {
            return new ResponseEntity<>(warehousesRepository.getAllWarehouses(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<WarehouseResponse> getWarehouseById(Long id) {
        try {
            Optional<Warehouses> optionalWarehouse = warehousesRepository.findById(id);
            if (optionalWarehouse.isPresent()) {
                Warehouses w = optionalWarehouse.get();
                WarehouseResponse response = new WarehouseResponse(w.getId(), w.getName());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<String> updateWarehouse(Long id, WarehouseRequest request) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Warehouses> optionalWarehouse = warehousesRepository.findById(id);
            if (optionalWarehouse.isPresent()) {
                Warehouses warehouse = optionalWarehouse.get();
                warehouse.setName(request.getName());
                warehousesRepository.save(warehouse);
                return WarehouseUtils.getResponseEntity("Warehouse updated successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Warehouse not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteWarehouse(Long id) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (warehousesRepository.existsById(id)) {
                warehousesRepository.deleteById(id);
                return WarehouseUtils.getResponseEntity("Warehouse deleted successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Warehouse not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
