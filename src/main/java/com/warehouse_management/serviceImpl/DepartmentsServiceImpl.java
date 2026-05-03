package com.warehouse_management.serviceImpl;

import com.warehouse_management.entity.Departments;
import com.warehouse_management.repositories.DepartmentRepository;
import com.warehouse_management.requests.DepartmentsRequest;
import com.warehouse_management.responses.DepartmentsResponse;
import com.warehouse_management.services.DepartmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentsServiceImpl implements DepartmentsService {

    private final DepartmentRepository departmentRepository;

    public DepartmentsServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public ResponseEntity<String> createDepartment(DepartmentsRequest request) {
        try {
            // Check if department name already exists
            if (departmentRepository.findByDepartmentName(request.getDepartmentName()).isPresent()) {
                return ResponseEntity.badRequest().body("Department name already exists");
            }

            Departments department = new Departments();
            department.setDepartmentName(request.getDepartmentName());

            departmentRepository.save(department);
            return ResponseEntity.ok("Department created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateDepartment(Long id, DepartmentsRequest request) {
        try {
            Optional<Departments> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isEmpty()) {
                return ResponseEntity.badRequest().body("Department not found");
            }

            Departments department = optionalDepartment.get();

            if (request.getDepartmentName() != null && !request.getDepartmentName().isEmpty()) {
                // Check if new name already exists (excluding current department)
                Optional<Departments> existingDept = departmentRepository.findByDepartmentName(request.getDepartmentName());
                if (existingDept.isPresent() && !existingDept.get().getId().equals(id)) {
                    return ResponseEntity.badRequest().body("Department name already exists");
                }
                department.setDepartmentName(request.getDepartmentName());
            }

            departmentRepository.save(department);
            return ResponseEntity.ok("Department updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteDepartment(Long id) {
        try {
            Optional<Departments> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isEmpty()) {
                return ResponseEntity.badRequest().body("Department not found");
            }

            departmentRepository.deleteById(id);
            return ResponseEntity.ok("Department deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<DepartmentsResponse> getDepartmentById(Long id) {
        try {
            Optional<Departments> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Departments department = optionalDepartment.get();
            return ResponseEntity.ok(mapToResponse(department));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<DepartmentsResponse>> getAllDepartments() {
        try {
            List<Departments> departments = departmentRepository.findAll();
            List<DepartmentsResponse> responses = new ArrayList<>();
            for (Departments department : departments) {
                responses.add(mapToResponse(department));
            }
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<DepartmentsResponse>> getDepartmentsByName(String departmentName) {
        try {
            List<Departments> departments = departmentRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
            List<DepartmentsResponse> responses = new ArrayList<>();
            for (Departments department : departments) {
                responses.add(mapToResponse(department));
            }
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private DepartmentsResponse mapToResponse(Departments department) {
        DepartmentsResponse response = new DepartmentsResponse();
        response.setId(department.getId());
        response.setDepartmentName(department.getDepartmentName());
        return response;
    }
}