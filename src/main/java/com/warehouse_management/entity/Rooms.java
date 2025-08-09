package com.warehouse_management.entity;
import jakarta.persistence.*;
@NamedQuery(
        name = "Rooms.getAllRooms",
        query = "select new com.warehouse_management.wrapper.RoomsWrapper(r.id, r.name,r.floors.name,r.floors.warehouses.name) from Rooms r"
)
@NamedQuery(
        name = "Rooms.getAllRoomsByWarehouseId",
        query = "select new com.warehouse_management.wrapper.RoomsWrapper(r.id, r.name, r.floors.name, r.floors.warehouses.name) " +
                "from Rooms r where r.floors.warehouses.id = :warehouseId"
)

@Entity
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private Floors floors;

    public Rooms() {
    }

    public Rooms(Long id, String name, Floors floors) {
        this.id = id;
        this.name = name;
        this.floors = floors;
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

    public Floors getFloors() {
        return floors;
    }

    public void setFloors(Floors floors) {
        this.floors = floors;
    }
}
