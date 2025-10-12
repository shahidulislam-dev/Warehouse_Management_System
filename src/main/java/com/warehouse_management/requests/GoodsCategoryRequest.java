package com.warehouse_management.requests;

public class GoodsCategoryRequest {
    private String name;
    private String unit;
    private String sizeUnit;

    public GoodsCategoryRequest() {}

    public GoodsCategoryRequest(String name, String unit, String sizeUnit) {
        this.name = name;
        this.unit = unit;
        this.sizeUnit = sizeUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }
}
