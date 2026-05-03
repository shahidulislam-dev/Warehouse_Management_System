package com.warehouse_management.controllersImpl;

import com.warehouse_management.controllers.DepartmentsController;
import com.warehouse_management.requests.DepartmentsRequest;
import com.warehouse_management.responses.DepartmentsResponse;
import com.warehouse_management.services.DepartmentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentsControllerImpl implements DepartmentsController {

    private final DepartmentsService departmentsService;

    public DepartmentsControllerImpl(DepartmentsService departmentsService) {
        this.departmentsService = departmentsService;
    }

    @Override
    public ResponseEntity<String> create(DepartmentsRequest request) {
        return departmentsService.createDepartment(request);
    }

    @Override
    public ResponseEntity<String> update(Long id, DepartmentsRequest request) {
        return departmentsService.updateDepartment(id, request);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        return departmentsService.deleteDepartment(id);
    }

    @Override
    public ResponseEntity<DepartmentsResponse> getById(Long id) {
        return departmentsService.getDepartmentById(id);
    }

    @Override
    public ResponseEntity<List<DepartmentsResponse>> getAll() {
        return departmentsService.getAllDepartments();
    }

    @Override
    public ResponseEntity<List<DepartmentsResponse>> getByName(String departmentName) {
        return departmentsService.getDepartmentsByName(departmentName);
    }
}