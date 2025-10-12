package com.warehouse_management.wrapper;

public class GoodsCategoryWrapper {
    private Long id;
    private String name;
    private String unit;
    private String sizeUnit;
    public GoodsCategoryWrapper() {}

    public GoodsCategoryWrapper(Long id, String name, String unit, String sizeUnit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.sizeUnit = sizeUnit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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

