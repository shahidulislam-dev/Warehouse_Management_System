package com.warehouse_management.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class GoodsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String unit;

    @OneToMany(mappedBy = "category")
    private List<Goods> goodsList;

    public GoodsCategory() {
    }

    public GoodsCategory(Long id, String name, String unit, List<Goods> goodsList) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.goodsList = goodsList;
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

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

