package com.warehouse_management.entity;

import jakarta.persistence.*;

import java.util.List;
@NamedQuery(name = "Warehouses.getAllWarehouses", query = "select new com.warehouse_management.wrapper.WarehouseWrapper(w.id, w.name) from Warehouses w")
@Entity

public class Warehouses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "warehouses", cascade = CascadeType.ALL)
    private List<Floors> floors;

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

    public List<Floors> getFloors() {
        return floors;
    }

    public void setFloors(List<Floors> floors) {
        this.floors = floors;
    }
}
