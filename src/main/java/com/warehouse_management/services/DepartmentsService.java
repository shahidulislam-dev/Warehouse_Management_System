package com.warehouse_management.services;

import com.warehouse_management.requests.DepartmentsRequest;
import com.warehouse_management.responses.DepartmentsResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface DepartmentsService {

    ResponseEntity<String> createDepartment(DepartmentsRequest request);

    ResponseEntity<String> updateDepartment(Long id, DepartmentsRequest request);

    ResponseEntity<String> deleteDepartment(Long id);

    ResponseEntity<DepartmentsResponse> getDepartmentById(Long id);

    ResponseEntity<List<DepartmentsResponse>> getAllDepartments();

    ResponseEntity<List<DepartmentsResponse>> getDepartmentsByName(String departmentName);
}