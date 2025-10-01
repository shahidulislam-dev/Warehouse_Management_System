package com.warehouse_management.requests;

public class GoodsCategoryRequest {
    private String name;

    public GoodsCategoryRequest() {}

    public GoodsCategoryRequest(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
