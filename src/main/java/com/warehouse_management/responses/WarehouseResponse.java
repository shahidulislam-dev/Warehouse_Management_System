package com.warehouse_management.responses;

public class WarehouseResponse {
    private Long id;
    private String name;

    public WarehouseResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public WarehouseResponse() {
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
}

