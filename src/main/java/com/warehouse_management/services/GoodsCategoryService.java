package com.warehouse_management.services;

import com.warehouse_management.requests.GoodsCategoryRequest;
import com.warehouse_management.responses.GoodsCategoryResponse;
import com.warehouse_management.wrapper.GoodsCategoryWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoodsCategoryService {
    ResponseEntity<String> createCategory(GoodsCategoryRequest request);

    ResponseEntity<List<GoodsCategoryWrapper>> getAllCategories();

    ResponseEntity<GoodsCategoryResponse> getCategoryById(Long id);

    ResponseEntity<String> updateCategory(Long id, GoodsCategoryRequest request);

    ResponseEntity<String> deleteCategory(Long id);
}
