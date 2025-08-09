package com.warehouse_management.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private GoodsCategory category;

    private int quantity;
    private String unit;

    private String source;
    private String donatedOrBoughtBy;
    private String approvedByName;

    @ManyToOne
    private Rooms rooms;

}
