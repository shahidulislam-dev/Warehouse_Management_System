package com.warehouse_management.serviceImpl;

import com.warehouse_management.entity.GoodsCategory;
import com.warehouse_management.repositories.GoodsCategoryRepository;
import com.warehouse_management.requests.GoodsCategoryRequest;
import com.warehouse_management.responses.GoodsCategoryResponse;
import com.warehouse_management.services.GoodsCategoryService;
import com.warehouse_management.wrapper.GoodsCategoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    private final GoodsCategoryRepository goodsCategoryRepository;

    @Autowired
    public GoodsCategoryServiceImpl(GoodsCategoryRepository goodsCategoryRepository) {
        this.goodsCategoryRepository = goodsCategoryRepository;
    }

    @Override
    public ResponseEntity<String> createCategory(GoodsCategoryRequest request) {
        try {
            GoodsCategory category = new GoodsCategory();
            category.setName(request.getName());
            category.setUnit(request.getUnit());
            goodsCategoryRepository.save(category);
            return ResponseEntity.ok("Goods category created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while creating category");
        }
    }

    @Override
    public ResponseEntity<List<GoodsCategoryWrapper>> getAllCategories() {
        try {
            List<GoodsCategoryWrapper> categories = goodsCategoryRepository.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @Override
    public ResponseEntity<GoodsCategoryResponse> getCategoryById(Long id) {
        try {
            Optional<GoodsCategory> optionalCategory = goodsCategoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                GoodsCategory c = optionalCategory.get();
                GoodsCategoryResponse response = new GoodsCategoryResponse(c.getId(), c.getName(), c.getUnit());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> updateCategory(Long id, GoodsCategoryRequest request) {
        try {
            Optional<GoodsCategory> optionalCategory = goodsCategoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                GoodsCategory category = optionalCategory.get();
                category.setName(request.getName());
                category.setUnit(request.getUnit());
                goodsCategoryRepository.save(category);
                return ResponseEntity.ok("Goods category updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while updating category");
        }
    }

    @Override
    public ResponseEntity<String> deleteCategory(Long id) {
        try {
            if (goodsCategoryRepository.existsById(id)) {
                goodsCategoryRepository.deleteById(id);
                return ResponseEntity.ok("Goods category deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while deleting category");
        }
    }
}

