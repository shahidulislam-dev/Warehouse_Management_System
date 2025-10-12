package com.warehouse_management.repositories;

import com.warehouse_management.entity.GoodsCategory;
import com.warehouse_management.wrapper.GoodsCategoryWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory, Long> {

    @Query("select new com.warehouse_management.wrapper.GoodsCategoryWrapper(gc.id, gc.name, gc.unit, gc.sizeUnit) from GoodsCategory gc")
    List<GoodsCategoryWrapper> getAllCategories();
}
