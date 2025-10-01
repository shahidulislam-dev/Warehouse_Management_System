package com.warehouse_management.responses;

public class GoodsResponse {
    private Long id;
    private String name;
    private int quantity;
    private String unit;
    private String categoryName;
    private String roomName;
    private String floorName;
    private String warehouseName;
    private String createdBy;

    public GoodsResponse() {}

    public GoodsResponse(Long id, String name, int quantity, String unit,
                         String categoryName, String roomName, String floorName,
                         String warehouseName, String createdBy) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.categoryName = categoryName;
        this.roomName = roomName;
        this.floorName = floorName;
        this.warehouseName = warehouseName;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
