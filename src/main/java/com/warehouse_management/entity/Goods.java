package com.warehouse_management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(
                name = "Goods.getAllGoods",
                query = "select new com.warehouse_management.wrapper.GoodsWrapper(" +
                        "g.id, g.name, g.quantity, g.size," +
                        "g.category.name, g.category.unit, g.category.sizeUnit, " +
                        "g.rooms.name, g.floors.name, g.warehouses.name, g.createdBy.fullName, g.createDate, g.updateDate) " +
                        "from Goods g"
        ),
        @NamedQuery(
                name = "Goods.getGoodsByWarehouseId",
                query = "select new com.warehouse_management.wrapper.GoodsWrapper(" +
                        "g.id, g.name, g.quantity, g.size, " +
                        "g.category.name, g.category.unit, g.category.sizeUnit, " +
                        "g.rooms.name, g.floors.name, g.warehouses.name, g.createdBy.fullName, g.createDate, g.updateDate) " +
                        "from Goods g where g.warehouses.id = :warehouseId"
        ),
        @NamedQuery(
                name = "Goods.getGoodsByFloorId",
                query = "select new com.warehouse_management.wrapper.GoodsWrapper(" +
                        "g.id, g.name, g.quantity, g.size, " +
                        "g.category.name, g.category.unit, g.category.sizeUnit, " +
                        "g.rooms.name, g.floors.name, g.warehouses.name, g.createdBy.fullName, g.createDate, g.updateDate) " +
                        "from Goods g where g.floors.id = :floorId"
        ),
        @NamedQuery(
                name = "Goods.getGoodsByRoomId",
                query = "select new com.warehouse_management.wrapper.GoodsWrapper(" +
                        "g.id, g.name, g.quantity, g.size, " +
                        "g.category.name, g.category.unit, g.category.sizeUnit, " +
                        "g.rooms.name, g.floors.name, g.warehouses.name, g.createdBy.fullName, g.createDate, g.updateDate) " +
                        "from Goods g where g.rooms.id = :roomId"
        )
})
@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private Long size;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private GoodsCategory category;

    @ManyToOne
    private Rooms rooms;

    @ManyToOne
    private Floors floors;

    @ManyToOne
    private Warehouses warehouses;

    public Goods() {}

    public Goods(Long id, String name, int quantity,Long size, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.createDate = createDate;
        this.updateDate = updateDate;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public GoodsCategory getCategory() {
        return category;
    }

    public void setCategory(GoodsCategory category) {
        this.category = category;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public Floors getFloors() {
        return floors;
    }

    public void setFloors(Floors floors) {
        this.floors = floors;
    }

    public Warehouses getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Warehouses warehouses) {
        this.warehouses = warehouses;
    }

}