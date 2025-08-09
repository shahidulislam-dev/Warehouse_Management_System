package com.warehouse_management.entity;

import jakarta.persistence.*;

import java.util.List;
@NamedQuery(
        name = "Floors.getAllFloors",
        query = "select new com.warehouse_management.wrapper.FloorWrapper(f.id, f.name, f.warehouses.name) from Floors f"
)
@Entity
public class Floors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private Warehouses warehouses;

    @OneToMany(mappedBy = "floors", cascade = CascadeType.ALL)
    private List<Rooms> rooms;

    public Floors() {
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

    public Warehouses getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Warehouses warehouses) {
        this.warehouses = warehouses;
    }

    public List<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(List<Rooms> rooms) {
        this.rooms = rooms;
    }
}
