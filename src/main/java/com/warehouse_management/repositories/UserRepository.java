package com.warehouse_management.repositories;

import com.warehouse_management.entity.User;
import com.warehouse_management.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailId(@Param("email") String email);

    // Add @Query annotation for custom methods
    @Query("select new com.warehouse_management.wrapper.UserWrapper(u.id, u.fullName, u.email, u.contactNumber, u.status, u.role) from User u where (u.role = 'admin' or u.role = 'staff')")
    List<UserWrapper> getAllUsersForSuperAdmin();

    @Query("select new com.warehouse_management.wrapper.UserWrapper(u.id, u.fullName, u.email, u.contactNumber, u.status, u.role) from User u where u.role = 'staff'")
    List<UserWrapper> getStaffUsers();

    List<String> getAllAdmin();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    User findByEmail(String email);

    long countByRole(String role);
}