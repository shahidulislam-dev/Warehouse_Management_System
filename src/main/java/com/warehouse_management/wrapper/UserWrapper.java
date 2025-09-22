package com.warehouse_management.wrapper;

public class UserWrapper {
    private Long id;
    private String fullName;
    private String email;
    private String contactNumber;
    private String status;
    private String role;

    public UserWrapper(Long id, String fullName, String email, String contactNumber, String status, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.status = status;
        this.role = role;
    }

    public UserWrapper() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}