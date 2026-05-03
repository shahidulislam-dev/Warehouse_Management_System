package com.warehouse_management.repositories;

import com.warehouse_management.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Events, Long> {

    Optional<Events> findById(Long id);

    List<Events> findByEventNameContainingIgnoreCase(String eventName);

    Optional<Events> findByEventName(String eventName);

    List<Events> findByActiveTrue();
}