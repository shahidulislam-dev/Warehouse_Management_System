package com.warehouse_management.responses;

public class RoomResponse {
    private Long id;
    private String name;
    private String floorName;
    private String warehouseName;

    public RoomResponse() {
    }

    public RoomResponse(Long id, String name, String floorName, String warehouseName) {
        this.id = id;
        this.name = name;
        this.floorName = floorName;
        this.warehouseName = warehouseName;
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
}
