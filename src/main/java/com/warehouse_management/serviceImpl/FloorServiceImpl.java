package com.warehouse_management.serviceImpl;

import com.warehouse_management.constants.WarehouseConstant;
import com.warehouse_management.entity.Floors;
import com.warehouse_management.entity.Warehouses;
import com.warehouse_management.jwt.JwtFilter;
import com.warehouse_management.repositories.FloorRepository;
import com.warehouse_management.repositories.WarehouseRepository;
import com.warehouse_management.requests.FloorRequest;
import com.warehouse_management.responses.FloorResponse;
import com.warehouse_management.responses.WarehouseResponse;
import com.warehouse_management.services.FloorService;
import com.warehouse_management.utils.WarehouseUtils;
import com.warehouse_management.wrapper.FloorWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FloorServiceImpl implements FloorService {
    private final FloorRepository floorRepository;
    private final WarehouseRepository warehouseRepository;
    private final JwtFilter jwtFilter;

    public FloorServiceImpl(FloorRepository floorRepository, WarehouseRepository warehouseRepository, JwtFilter jwtFilter) {
        this.floorRepository = floorRepository;
        this.warehouseRepository = warehouseRepository;
        this.jwtFilter = jwtFilter;
    }
    @Override
    public ResponseEntity<String> createFloor(FloorRequest request) {
        try {
            Optional<Warehouses> warehouse = warehouseRepository.findById(request.getWarehouseId());

            if (warehouse.isEmpty()) {
                return WarehouseUtils.getResponseEntity("Warehouse does not exist", HttpStatus.OK);
            }
            Floors floor = new Floors();
            floor.setName(request.getName());
            floor.setWarehouses(warehouse.get());
            floorRepository.save(floor);

            return WarehouseUtils.getResponseEntity("Floor created successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<List<FloorWrapper>> getAllFloors() {
        try{
            return new ResponseEntity<>(floorRepository.getAllFloors(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<FloorResponse> getFloorById(Long id) {
        try {
            Optional<Floors> optionalFloors = floorRepository.findById(id);
            if (optionalFloors.isPresent()) {
                Floors f = optionalFloors.get();
                FloorResponse response = new FloorResponse(f.getId(), f.getName(), f.getWarehouses().getName());
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
    public ResponseEntity<List<FloorWrapper>> getFloorsByWarehouseId(Long warehouseId) {
        try {
            List<Floors> floorsList = floorRepository.findByWarehouses_Id(warehouseId);
            List<FloorWrapper> floorWrappers = floorsList.stream()
                    .map(floor -> new FloorWrapper(
                            floor.getId(),
                            floor.getName(),
                            floor.getWarehouses().getName()
                    ))
                    .toList();
            return ResponseEntity.ok(floorWrappers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }



    @Override
    public ResponseEntity<String> updateFloor(Long id, FloorRequest request) {
        try {
            if (!jwtFilter.isAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Floors> optionalFloors = floorRepository.findById(id);
            if (optionalFloors.isPresent()) {
                Floors floors = optionalFloors.get();
                floors.setName(request.getName());
                floorRepository.save(floors);
                return WarehouseUtils.getResponseEntity("Floor updated successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Floor not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteFloor(Long id) {
        try {
            if (!jwtFilter.isAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (floorRepository.existsById(id)) {
                floorRepository.deleteById(id);
                return WarehouseUtils.getResponseEntity("Floor deleted successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Floor not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
