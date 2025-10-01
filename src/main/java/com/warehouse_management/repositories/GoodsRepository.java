package com.warehouse_management.repositories;

import com.warehouse_management.entity.Goods;
import com.warehouse_management.wrapper.GoodsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    @Query(name = "Goods.getAllGoods")
    List<GoodsWrapper> getAllGoods();

    @Query(name = "Goods.getGoodsByWarehouseId")
    List<GoodsWrapper> getGoodsByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query(name = "Goods.getGoodsByFloorId")
    List<GoodsWrapper> getGoodsByFloorId(@Param("floorId") Long floorId);

    @Query(name = "Goods.getGoodsByRoomId")
    List<GoodsWrapper> getGoodsByRoomId(@Param("roomId") Long roomId);
}
