package com.warehouse_management.controllers;

import com.warehouse_management.requests.GoodsCategoryRequest;
import com.warehouse_management.responses.GoodsCategoryResponse;
import com.warehouse_management.wrapper.GoodsCategoryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/api/goods-category")
public interface GoodsCategoryController {
    @PostMapping("/create")
    ResponseEntity<String> createCategory(GoodsCategoryRequest request);
    @PutMapping("/update/{id}")
    ResponseEntity<String> updateCategory(@PathVariable Long id, GoodsCategoryRequest request);
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable Long id);
    @GetMapping("/{id}")
    ResponseEntity<GoodsCategoryResponse> getCategoryById(@PathVariable  Long id);
    @GetMapping("/all")
    ResponseEntity<List<GoodsCategoryWrapper>> getAllCategories();
}
