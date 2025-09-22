package com.warehouse_management.serviceImpl;

import com.warehouse_management.entity.User;
import com.warehouse_management.jwt.CustomUserDetailsService;
import com.warehouse_management.jwt.JwtFilter;
import com.warehouse_management.jwt.JwtUtil;
import com.warehouse_management.repositories.UserRepository;
import com.warehouse_management.services.AuthService;
import com.google.common.base.Strings;
import com.warehouse_management.utils.EmailUtils;
import com.warehouse_management.utils.WarehouseUtils;
import com.warehouse_management.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    CustomUserDetailsService customUserDetailsService;
    JwtUtil jwtUtil;
    JwtFilter jwtFilter;
    EmailUtils emailUtils;
    PasswordEncoder passwordEncoder;
    @Autowired
    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil, JwtFilter jwtFilter, EmailUtils emailUtils,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.emailUtils = emailUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try {
            if (validateSignupMap(requestMap)){
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(getUserFromMap(requestMap));
                    return WarehouseUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                }else {
                    return WarehouseUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }

            }else {
                return WarehouseUtils.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if (customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil
                            .generateToken(customUserDetailsService.getUserDetails().getEmail(),
                                    customUserDetailsService.getUserDetails().getRole()) + "\"}", HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("{\"message\":\""+"Wait for admin approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
        return new ResponseEntity<>("{\"message\":\""+"Bad credential."+"\"}",
                HttpStatus.BAD_REQUEST);
    }

    // AuthServiceImpl.java - More efficient approach
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin() || jwtFilter.isSuperAdmin()){
                List<UserWrapper> users;

                if (jwtFilter.isSuperAdmin()) {
                    // Super Admin: get all users (admins and staff)
                    users = userRepository.getAllUsersForSuperAdmin();
                } else {
                    // Admin: get only staff users
                    users = userRepository.getStaffUsers();
                }

                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin() || jwtFilter.isSuperAdmin()){
                Optional<User> optional = userRepository.findById(Long.parseLong(requestMap.get("id")));
                if(optional.isPresent()){
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepository.getAllAdmin());
                    return WarehouseUtils.getResponseEntity("User status updated successfully.",HttpStatus.OK);
                }else {
                    return WarehouseUtils.getResponseEntity("User doesn't exist",HttpStatus.OK);
                }
            }else {
                return WarehouseUtils.getResponseEntity("Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.simpleMailMessage(jwtFilter.getCurrentUser(),"Account approved", "USER:- "+user+"\n is approved by \nAdmin:-"+jwtFilter.getCurrentUser(),allAdmin);
        }else {
            emailUtils.simpleMailMessage(jwtFilter.getCurrentUser(),"Account disabled", "USER:- "+user+"\n is disabled by \nAdmin:-"+jwtFilter.getCurrentUser(),allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return WarehouseUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if(userObj != null){
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userRepository.save(userObj);
                    return WarehouseUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                }else{
                    return WarehouseUtils.getResponseEntity("Incorrect old Password", HttpStatus.BAD_REQUEST);
                }
            }
            return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgetMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
            return WarehouseUtils.getResponseEntity("Check mail for get Credentials.", HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSignupMap(Map<String, String> requestMap){
        return requestMap.containsKey("fullName") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setFullName(requestMap.get("fullName"));
        user.setContactNUmber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("staff");
        return user;
    }



    //Create a super admin
    @Override
    public ResponseEntity<String> createSuperAdmin(Map<String, String> requestMap) {
        try {
            long superAdminCount = userRepository.countByRole("super-admin");
            if (superAdminCount > 0) {
                return WarehouseUtils.getResponseEntity("Super-admin already exists. Use admin panel to create new ones.", HttpStatus.BAD_REQUEST);
            }

            if (validateSignupMap(requestMap)){
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    User superAdmin = new User();
                    superAdmin.setFullName(requestMap.get("fullName"));
                    superAdmin.setContactNUmber(requestMap.get("contactNumber"));
                    superAdmin.setEmail(requestMap.get("email"));
                    superAdmin.setPassword(passwordEncoder.encode(requestMap.get("password")));
                    superAdmin.setStatus("true");
                    superAdmin.setRole("super-admin");

                    userRepository.save(superAdmin);
                    return WarehouseUtils.getResponseEntity("Super-admin created successfully!", HttpStatus.OK);
                }else {
                    return WarehouseUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            }else {
                return WarehouseUtils.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



