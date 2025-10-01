package com.warehouse_management.requests;

public class GoodsRequest {
    private String name;
    private int quantity;
    private String unit;
    private Long categoryId;
    private Long roomId;
    private Long floorId;
    private Long warehouseId;

    public GoodsRequest() {}

    public GoodsRequest(String name, int quantity, String unit,
                        Long categoryId, Long roomId, Long floorId, Long warehouseId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.categoryId = categoryId;
        this.roomId = roomId;
        this.floorId = floorId;
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
