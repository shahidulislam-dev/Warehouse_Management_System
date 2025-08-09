package com.warehouse_management.repositories;

import com.warehouse_management.entity.Rooms;
import com.warehouse_management.wrapper.FloorWrapper;
import com.warehouse_management.wrapper.RoomsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    List<RoomsWrapper> getAllRooms();
    List<Rooms> findByFloors_IdAndFloors_Warehouses_Id(Long floorId, Long warehouseId);
    @Query(name = "Rooms.getAllRoomsByWarehouseId")
    List<RoomsWrapper> getAllRoomsByWarehouseId(@Param("warehouseId") Long warehouseId);

}
