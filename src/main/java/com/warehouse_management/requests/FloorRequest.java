package com.warehouse_management.requests;

public class FloorRequest {
    private String name;
    private Long warehouseId;

    public FloorRequest() {
    }

    public FloorRequest(String name, Long warehouseId) {
        this.name = name;
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
