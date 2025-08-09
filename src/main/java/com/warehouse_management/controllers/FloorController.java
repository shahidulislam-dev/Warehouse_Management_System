package com.warehouse_management.controllers;

import com.warehouse_management.requests.FloorRequest;
import com.warehouse_management.responses.FloorResponse;
import com.warehouse_management.wrapper.FloorWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/floor")
public interface FloorController {

    @PostMapping("/create")
    ResponseEntity<String> create(@RequestBody FloorRequest request);

    @GetMapping("/get/all")
    ResponseEntity<List<FloorWrapper>> getAll();

    @GetMapping("/get/{id}")
    ResponseEntity<FloorResponse> getById(@PathVariable Long id);

    @GetMapping("/get/by-warehouse/{warehouseId}")
    ResponseEntity<List<FloorWrapper>> getByWarehouseId(@PathVariable Long warehouseId);


    @PutMapping("/update/{id}")
    ResponseEntity<String> update(@PathVariable Long id, @RequestBody FloorRequest request);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id);
}
