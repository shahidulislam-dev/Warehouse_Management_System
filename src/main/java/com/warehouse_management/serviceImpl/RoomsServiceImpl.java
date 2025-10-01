package com.warehouse_management.serviceImpl;

import com.warehouse_management.constants.WarehouseConstant;
import com.warehouse_management.entity.Floors;
import com.warehouse_management.entity.Rooms;
import com.warehouse_management.jwt.JwtFilter;
import com.warehouse_management.repositories.FloorRepository;
import com.warehouse_management.repositories.RoomsRepository;
import com.warehouse_management.requests.RoomRequest;
import com.warehouse_management.responses.RoomResponse;
import com.warehouse_management.services.RoomsService;
import com.warehouse_management.utils.WarehouseUtils;
import com.warehouse_management.wrapper.RoomsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final RoomsRepository roomsRepository;
    private final FloorRepository floorRepository;
    private final JwtFilter jwtFilter;
    @Autowired
    public RoomsServiceImpl(RoomsRepository roomsRepository, FloorRepository floorRepository, JwtFilter jwtFilter) {
        this.roomsRepository = roomsRepository;
        this.floorRepository = floorRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> createFloor(RoomRequest request) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Floors> floor = floorRepository.findById(request.getFloorId());
            if (floor.isEmpty()) {
                return WarehouseUtils.getResponseEntity("Floor does not exist", HttpStatus.OK);
            }

            Rooms room = new Rooms();
            room.setName(request.getName());
            room.setFloors(floor.get());
            roomsRepository.save(room);

            return WarehouseUtils.getResponseEntity("Room created successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RoomsWrapper>> getAllFloors() {
        try {
            return new ResponseEntity<>(roomsRepository.getAllRooms(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<RoomResponse> getFloorById(Long id) {
        try {
            Optional<Rooms> optionalRoom = roomsRepository.findById(id);
            if (optionalRoom.isPresent()) {
                Rooms r = optionalRoom.get();
                RoomResponse response = new RoomResponse(
                        r.getId(),
                        r.getName(),
                        r.getFloors().getName(),
                        r.getFloors().getWarehouses().getName()
                );
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
    public ResponseEntity<List<RoomsWrapper>> getRoomsByFloorAndWarehouse(Long floorId, Long warehouseId) {
        List<Rooms> rooms = roomsRepository.findByFloors_IdAndFloors_Warehouses_Id(floorId, warehouseId);
        List<RoomsWrapper> wrappers = rooms.stream()
                .map(r -> new RoomsWrapper(r.getId(), r.getName(), r.getFloors().getName(), r.getFloors().getWarehouses().getName()))
                .toList();
        return ResponseEntity.ok(wrappers);
    }

    @Override
    public ResponseEntity<List<RoomsWrapper>> getRoomsByWarehouseId(Long warehouseId) {
        try {
            return new ResponseEntity<>(roomsRepository.getAllRoomsByWarehouseId(warehouseId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateFloor(Long id, RoomRequest request) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Rooms> optionalRoom = roomsRepository.findById(id);
            if (optionalRoom.isPresent()) {
                Rooms room = optionalRoom.get();
                room.setName(request.getName());
                roomsRepository.save(room);
                return WarehouseUtils.getResponseEntity("Room updated successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Room not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteFloor(Long id) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (roomsRepository.existsById(id)) {
                roomsRepository.deleteById(id);
                return WarehouseUtils.getResponseEntity("Room deleted successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Room not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
