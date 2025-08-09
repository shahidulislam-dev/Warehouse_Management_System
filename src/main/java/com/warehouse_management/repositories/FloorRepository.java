package com.warehouse_management.repositories;

import com.warehouse_management.entity.Floors;
import com.warehouse_management.wrapper.FloorWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floors, Long> {
    List<FloorWrapper> getAllFloors();
    List<Floors> findByWarehouses_Id(Long warehouseId);
}
