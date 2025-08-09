package com.warehouse_management.controllers;

import com.warehouse_management.requests.RoomRequest;
import com.warehouse_management.responses.RoomResponse;
import com.warehouse_management.wrapper.RoomsWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/room")
public interface RoomsController {
    @PostMapping("/create")
    ResponseEntity<String> create(@RequestBody RoomRequest request);

    @GetMapping("/get/all")
    ResponseEntity<List<RoomsWrapper>> getAll();

    @GetMapping("/get/{id}")
    ResponseEntity<RoomResponse> getById(@PathVariable Long id);

    @GetMapping("/get/by/floor/{floorId}/warehouse/{warehouseId}")
    ResponseEntity<List<RoomsWrapper>> getByWarehouseId(@PathVariable Long floorId, @PathVariable Long warehouseId);

    @GetMapping("/get/by-warehouse/{warehouseId}")
    ResponseEntity<List<RoomsWrapper>> getRoomsByWarehouse(@PathVariable Long warehouseId);

    @PutMapping("/update/{id}")
    ResponseEntity<String> update(@PathVariable Long id, @RequestBody RoomRequest request);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id);
}
