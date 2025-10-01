package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.GoodsCategoryController;
import com.warehouse_management.requests.GoodsCategoryRequest;
import com.warehouse_management.responses.GoodsCategoryResponse;
import com.warehouse_management.services.GoodsCategoryService;
import com.warehouse_management.wrapper.GoodsCategoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class GoodsCategoryControllerImpl implements GoodsCategoryController {
    private final GoodsCategoryService categoryService;

    @Autowired
    public GoodsCategoryControllerImpl(GoodsCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ResponseEntity<String> createCategory(@RequestBody GoodsCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @Override
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                 @RequestBody GoodsCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @Override
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @Override
    public ResponseEntity<GoodsCategoryResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }


    @Override
    public ResponseEntity<List<GoodsCategoryWrapper>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}