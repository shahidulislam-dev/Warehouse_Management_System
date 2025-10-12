package com.warehouse_management.responses;

public class GoodsResponse {
    private Long id;
    private String name;
    private int quantity;
    private Long size;
    private String categoryName;
    private String categoryUnit;
    private String sizeUnit;

    private String roomName;
    private String floorName;
    private String warehouseName;
    private String createdBy;


    public GoodsResponse(Long id, String name, int quantity,Long size,
                         String categoryName, String categoryUnit,String sizeUnit,
                         String roomName, String floorName, String warehouseName,
                         String createdBy) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.categoryName = categoryName;
        this.categoryUnit = categoryUnit;
        this.sizeUnit = sizeUnit;
        this.roomName = roomName;
        this.floorName = floorName;
        this.warehouseName = warehouseName;
        this.createdBy = createdBy;
    }



    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryUnit() { return categoryUnit; }
    public void setCategoryUnit(String categoryUnit) { this.categoryUnit = categoryUnit; }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}