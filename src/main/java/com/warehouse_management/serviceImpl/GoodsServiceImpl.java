package com.warehouse_management.serviceImpl;

import com.warehouse_management.constants.WarehouseConstant;
import com.warehouse_management.entity.*;
import com.warehouse_management.repositories.*;
import com.warehouse_management.requests.GoodsRequest;
import com.warehouse_management.responses.GoodsResponse;
import com.warehouse_management.services.GoodsService;
import com.warehouse_management.utils.WarehouseUtils;
import com.warehouse_management.jwt.JwtFilter;
import com.warehouse_management.wrapper.GoodsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsCategoryRepository categoryRepository;
    private final RoomsRepository roomRepository;
    private final FloorRepository floorRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final JwtFilter jwtFilter;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository, GoodsCategoryRepository categoryRepository,
                            RoomsRepository roomRepository, FloorRepository floorRepository,
                            WarehouseRepository warehouseRepository, UserRepository userRepository,
                            JwtFilter jwtFilter) {
        this.goodsRepository = goodsRepository;
        this.categoryRepository = categoryRepository;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> createGoods(GoodsRequest request) {
        try {
            String currentUserEmail = jwtFilter.getCurrentUser();
            User currentUser = userRepository.findByEmail(currentUserEmail);
            if(currentUser == null) {
                return WarehouseUtils.getResponseEntity("Current user not found.", HttpStatus.UNAUTHORIZED);
            }

            // Fetch category
            Optional<GoodsCategory> categoryOpt = categoryRepository.findById(request.getCategoryId());
            if (categoryOpt.isEmpty()) {
                return WarehouseUtils.getResponseEntity("Category not found", HttpStatus.NOT_FOUND);
            }

            // Fetch room
            Optional<Rooms> roomOpt = roomRepository.findById(request.getRoomId());
            if (roomOpt.isEmpty()) {
                return WarehouseUtils.getResponseEntity("Room not found", HttpStatus.NOT_FOUND);
            }

            // Fetch floor
            Optional<Floors> floorOpt = floorRepository.findById(request.getFloorId());
            if (floorOpt.isEmpty()) {
                return WarehouseUtils.getResponseEntity("Floor not found", HttpStatus.NOT_FOUND);
            }

            // Fetch warehouse
            Optional<Warehouses> warehouseOpt = warehouseRepository.findById(request.getWarehouseId());
            if (warehouseOpt.isEmpty()) {
                return WarehouseUtils.getResponseEntity("Warehouse not found", HttpStatus.NOT_FOUND);
            }

            Goods goods = new Goods();
            goods.setName(request.getName());
            goods.setQuantity(request.getQuantity());
            goods.setUnit(request.getUnit());
            goods.setCreateDate(LocalDateTime.now());
            goods.setUpdateDate(null);
            goods.setCategory(categoryOpt.get());
            goods.setRooms(roomOpt.get());
            goods.setFloors(floorOpt.get());
            goods.setWarehouses(warehouseOpt.get());
            goods.setCreatedBy(currentUser);

            goodsRepository.save(goods);
            return WarehouseUtils.getResponseEntity("Goods created successfully.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<List<GoodsWrapper>> getAllGoods() {
        try {
            return new ResponseEntity<>(goodsRepository.getAllGoods(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<GoodsResponse> getGoodsById(Long id) {
        try {
            Optional<Goods> optionalGoods = goodsRepository.findById(id);
            if (optionalGoods.isPresent()) {
                Goods g = optionalGoods.get();
                GoodsResponse response = new GoodsResponse(
                        g.getId(), g.getName(), g.getQuantity(), g.getUnit(),
                        g.getCategory().getName(),
                        g.getRooms().getName(),
                        g.getFloors().getName(),
                        g.getWarehouses().getName(),
                        g.getCreatedBy().getFullName()
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getGoodsByWarehouse(Long warehouseId) {
        return ResponseEntity.ok(goodsRepository.getGoodsByWarehouseId(warehouseId));
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getGoodsByFloor(Long floorId) {
        return ResponseEntity.ok(goodsRepository.getGoodsByFloorId(floorId));
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getGoodsByRoom(Long roomId) {
        return ResponseEntity.ok(goodsRepository.getGoodsByRoomId(roomId));
    }

    @Override
    public ResponseEntity<String> updateGoods(Long id, GoodsRequest request) {
        try {
            Optional<Goods> optionalGoods = goodsRepository.findById(id);
            if (optionalGoods.isPresent()) {
                Goods goods = optionalGoods.get();
                goods.setName(request.getName());
                goods.setQuantity(request.getQuantity());
                goods.setUnit(request.getUnit());
                goods.setUpdateDate(LocalDateTime.now());
                goodsRepository.save(goods);
                return WarehouseUtils.getResponseEntity("Goods updated successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Goods not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteGoods(Long id) {
        try {
            if (!jwtFilter.isAdmin() && !jwtFilter.isSuperAdmin()) {
                return WarehouseUtils.getResponseEntity(WarehouseConstant.UNAUTHORIZED_ACCESS,
                        HttpStatus.UNAUTHORIZED);
            }
            if (goodsRepository.existsById(id)) {
                goodsRepository.deleteById(id);
                return WarehouseUtils.getResponseEntity("Goods deleted successfully", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("Goods not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
