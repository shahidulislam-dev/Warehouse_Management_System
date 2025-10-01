package com.warehouse_management.services;

import com.warehouse_management.requests.GoodsRequest;
import com.warehouse_management.responses.GoodsResponse;
import com.warehouse_management.wrapper.GoodsWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoodsService {
    ResponseEntity<String> createGoods(GoodsRequest request);
    ResponseEntity<List<GoodsWrapper>> getAllGoods();
    ResponseEntity<GoodsResponse> getGoodsById(Long id);
    ResponseEntity<List<GoodsWrapper>> getGoodsByWarehouse(Long warehouseId);
    ResponseEntity<List<GoodsWrapper>> getGoodsByFloor(Long floorId);
    ResponseEntity<List<GoodsWrapper>> getGoodsByRoom(Long roomId);
    ResponseEntity<String> updateGoods(Long id, GoodsRequest request);
    ResponseEntity<String> deleteGoods(Long id);
}
