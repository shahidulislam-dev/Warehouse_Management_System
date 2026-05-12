package com.warehouse_management.requests;
@SuppressWarnings("unused")
public class RoomRequest {
    private String name;
    private Long floorId;

    public RoomRequest() {
    }

    public RoomRequest(String name, Long floorId) {
        this.name = name;
        this.floorId = floorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }
}
