package com.warehouse_management.responses;

public class DepartmentsResponse {

    private Long id;
    private String departmentName;

    public DepartmentsResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}