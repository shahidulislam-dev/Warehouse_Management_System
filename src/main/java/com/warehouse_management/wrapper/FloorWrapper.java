package com.warehouse_management.wrapper;

public class FloorWrapper {
    private Long id;
    private String name;
    private String warehouseName;

    public FloorWrapper() {
    }

    public FloorWrapper(Long id, String name, String warehouseName) {
        this.id = id;
        this.name = name;
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

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
