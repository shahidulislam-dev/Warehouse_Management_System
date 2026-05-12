package com.warehouse_management.controllers;

import com.warehouse_management.requests.WarehouseRequest;
import com.warehouse_management.responses.WarehouseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/warehouse")
public interface WarehouseController {
    @PostMapping("/create")
    ResponseEntity<WarehouseResponse> create(@RequestBody WarehouseRequest request);

    @GetMapping("/get/all")
    ResponseEntity<List<WarehouseResponse>> getAll();

    @GetMapping("get/{id}")
    ResponseEntity<WarehouseResponse> getById(@PathVariable Long id);

    @PutMapping("update/{id}")
    ResponseEntity<WarehouseResponse> update(@PathVariable Long id, @RequestBody WarehouseRequest request);

    @DeleteMapping("delete/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
