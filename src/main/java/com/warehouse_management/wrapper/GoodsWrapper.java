package com.warehouse_management.wrapper;

import java.time.LocalDateTime;

public class GoodsWrapper {
    private Long id;
    private String name;
    private int quantity;
    private String unit;
    private String categoryName;
    private String roomName;
    private String floorName;
    private String warehouseName;
    private String createdBy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public GoodsWrapper() {}

    public GoodsWrapper(Long id, String name, int quantity, String unit,
                        String categoryName, String roomName, String floorName,
                        String warehouseName, String createdBy,
                        LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.categoryName = categoryName;
        this.roomName = roomName;
        this.floorName = floorName;
        this.warehouseName = warehouseName;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    // getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }
}
