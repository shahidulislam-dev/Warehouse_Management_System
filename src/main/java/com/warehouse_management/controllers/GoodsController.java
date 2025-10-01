package com.warehouse_management.controllers;

import com.warehouse_management.requests.GoodsRequest;
import com.warehouse_management.responses.GoodsResponse;
import com.warehouse_management.wrapper.GoodsWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/goods")
public interface GoodsController {

    @PostMapping("/create")
    ResponseEntity<String> create(@RequestBody GoodsRequest request);

    @GetMapping("/get/all")
    ResponseEntity<List<GoodsWrapper>> getAll();

    @GetMapping("/get/{id}")
    ResponseEntity<GoodsResponse> getById(@PathVariable Long id);

    @GetMapping("/get/warehouse/{warehouseId}")
    ResponseEntity<List<GoodsWrapper>> getByWarehouse(@PathVariable Long warehouseId);

    @GetMapping("/get/floor/{floorId}")
    ResponseEntity<List<GoodsWrapper>> getByFloor(@PathVariable Long floorId);

    @GetMapping("/get/room/{roomId}")
    ResponseEntity<List<GoodsWrapper>> getByRoom(@PathVariable Long roomId);

    @PutMapping("/update/{id}")
    ResponseEntity<String> update(@PathVariable Long id, @RequestBody GoodsRequest request);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id);
}

