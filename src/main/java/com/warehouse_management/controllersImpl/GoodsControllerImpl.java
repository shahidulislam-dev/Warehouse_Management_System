package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.GoodsController;
import com.warehouse_management.requests.GoodsRequest;
import com.warehouse_management.responses.GoodsResponse;
import com.warehouse_management.services.GoodsService;
import com.warehouse_management.wrapper.GoodsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoodsControllerImpl implements GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsControllerImpl(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Override
    public ResponseEntity<String> create(GoodsRequest request) {
        return goodsService.createGoods(request);
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getAll() {
        return goodsService.getAllGoods();
    }

    @Override
    public ResponseEntity<GoodsResponse> getById(Long id) {
        return goodsService.getGoodsById(id);
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getByWarehouse(Long warehouseId) {
        return goodsService.getGoodsByWarehouse(warehouseId);
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getByFloor(Long floorId) {
        return goodsService.getGoodsByFloor(floorId);
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getByRoom(Long roomId) {
        return goodsService.getGoodsByRoom(roomId);
    }

    @Override
    public ResponseEntity<String> update(Long id, GoodsRequest request) {
        return goodsService.updateGoods(id, request);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        return goodsService.deleteGoods(id);
    }
}
