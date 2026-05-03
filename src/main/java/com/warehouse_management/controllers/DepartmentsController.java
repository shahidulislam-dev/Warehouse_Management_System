package com.warehouse_management.controllers;

import com.warehouse_management.requests.DepartmentsRequest;
import com.warehouse_management.responses.DepartmentsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/departments")
public interface DepartmentsController {

    @PostMapping("/create")
    ResponseEntity<String> create(@RequestBody DepartmentsRequest request);

    @PutMapping("/update/{id}")
    ResponseEntity<String> update(@PathVariable Long id, @RequestBody DepartmentsRequest request);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id);

    @GetMapping("/{id}")
    ResponseEntity<DepartmentsResponse> getById(@PathVariable Long id);

    @GetMapping("/all")
    ResponseEntity<List<DepartmentsResponse>> getAll();

    @GetMapping("/name/{departmentName}")
    ResponseEntity<List<DepartmentsResponse>> getByName(@PathVariable String departmentName);
}