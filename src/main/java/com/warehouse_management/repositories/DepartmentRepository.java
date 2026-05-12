package com.warehouse_management.repositories;

import com.warehouse_management.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Long> {
    @NonNull
    Optional<Departments> findById(@NonNull Long id);

    List<Departments> findByDepartmentNameContainingIgnoreCase(String departmentName);

    Optional<Departments> findByDepartmentName(String departmentName);
}