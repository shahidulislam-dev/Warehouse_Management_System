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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final EmailUtils emailUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           CustomUserDetailsService customUserDetailsService,
                           JwtUtil jwtUtil,
                           JwtFilter jwtFilter,
                           EmailUtils emailUtils,
                           PasswordEncoder passwordEncoder) {
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
            if (validateSignupMap(requestMap)) {
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(requestMap));
                    return WarehouseUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return WarehouseUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return WarehouseUtils.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            logger.error("Error during signup: {}", ex.getMessage());
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil
                            .generateToken(customUserDetailsService.getUserDetails().getEmail(),
                                    customUserDetailsService.getUserDetails().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("{\"message\":\"" + "Wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            logger.error("Authentication error: {}", ex.getMessage());
        }
        return new ResponseEntity<>("{\"message\":\"" + "Bad credential." + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isSuperAdmin()) {
                List<UserWrapper> users;
                if (jwtFilter.isSuperAdmin()) {
                    users = userRepository.getAllUsersForSuperAdmin();
                } else {
                    users = userRepository.getStaffUsers();
                }
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            logger.error("Error getting all users: {}", ex.getMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isSuperAdmin()) {
                Optional<User> optional = userRepository.findById(Long.parseLong(requestMap.get("id")));
                if (optional.isPresent()) {
                    User targetUser = optional.get();
                    String originalRole = targetUser.getRole();
                    String originalStatus = targetUser.getStatus();

                    if (!canModifyUser(targetUser)) {
                        return WarehouseUtils.getResponseEntity("Unauthorized to modify this user", HttpStatus.UNAUTHORIZED);
                    }

                    String status = requestMap.get("status");
                    String role = requestMap.get("role");

                    if (role != null && !isValidRoleUpdate(targetUser, role)) {
                        return WarehouseUtils.getResponseEntity("Invalid role update operation", HttpStatus.BAD_REQUEST);
                    }

                    boolean isRoleChanged = false;
                    boolean isStatusChanged = false;

                    if (status != null && role != null) {
                        userRepository.updateStatusAndRole(status, role, Long.parseLong(requestMap.get("id")));
                        isStatusChanged = !status.equals(originalStatus);
                        isRoleChanged = !role.equals(originalRole);
                        logger.info("Updated both status and role for user ID: {}", requestMap.get("id"));
                    } else if (status != null) {
                        userRepository.updateStatus(status, Integer.parseInt(requestMap.get("id")));
                        isStatusChanged = !status.equals(originalStatus);
                        logger.info("Updated status only for user ID: {}", requestMap.get("id"));
                    } else if (role != null) {
                        isRoleChanged = !role.equals(originalRole);
                        targetUser.setRole(role);
                        userRepository.save(targetUser);
                        logger.info("Updated role only for user ID: {}", requestMap.get("id"));
                    } else {
                        return WarehouseUtils.getResponseEntity("No update parameters provided", HttpStatus.BAD_REQUEST);
                    }

                    // Send notifications to all admins and super-admins
                    List<String> allAdminsAndSuperAdmins = getAllAdminsAndSuperAdmins();

                    if (isStatusChanged) {
                        sendStatusUpdateNotification(targetUser.getEmail(), originalStatus, status, allAdminsAndSuperAdmins);
                    }

                    if (isRoleChanged) {
                        sendRoleUpdateNotification(targetUser.getEmail(), originalRole, role, allAdminsAndSuperAdmins);
                    }

                    return WarehouseUtils.getResponseEntity("User updated successfully.", HttpStatus.OK);
                } else {
                    return WarehouseUtils.getResponseEntity("User doesn't exist", HttpStatus.NOT_FOUND);
                }
            } else {
                return WarehouseUtils.getResponseEntity("Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }
        } catch (NumberFormatException ex) {
            logger.error("Invalid user ID format: {}", ex.getMessage());
            return WarehouseUtils.getResponseEntity("Invalid user ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Error updating user: {}", ex.getMessage());
        }
        return WarehouseUtils.getResponseEntity("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return WarehouseUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (userObj != null) {
                if (passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
                    userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                    userRepository.save(userObj);
                    return WarehouseUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                } else {
                    return WarehouseUtils.getResponseEntity("Incorrect old Password", HttpStatus.BAD_REQUEST);
                }
            }
            return WarehouseUtils.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Error changing password: {}", ex.getMessage());
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                emailUtils.forgetMail(user.getEmail(), "Credentials by Warehouse Management System", user.getPassword());
                return WarehouseUtils.getResponseEntity("Check mail for credentials.", HttpStatus.OK);
            } else {
                return WarehouseUtils.getResponseEntity("User not found with this email", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Error in forgot password: {}", ex.getMessage());
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> createSuperAdmin(Map<String, String> requestMap) {
        try {
            long superAdminCount = userRepository.countByRole("super-admin");
            if (superAdminCount >= 3) {
                return WarehouseUtils.getResponseEntity("Maximum super-admin limit reached (3).", HttpStatus.BAD_REQUEST);
            }

            if (validateSignupMap(requestMap)) {
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    User superAdmin = new User();
                    superAdmin.setFullName(requestMap.get("fullName"));
                    superAdmin.setContactNUmber(requestMap.get("contactNumber"));
                    superAdmin.setEmail(requestMap.get("email"));
                    superAdmin.setPassword(passwordEncoder.encode(requestMap.get("password")));
                    superAdmin.setStatus("true");
                    superAdmin.setRole("super-admin");

                    userRepository.save(superAdmin);

                    // Notify all admins and super-admins about new super-admin creation
                    List<String> allAdminsAndSuperAdmins = getAllAdminsAndSuperAdmins();
                    sendNewSuperAdminNotification(superAdmin.getEmail(), superAdmin.getFullName(), allAdminsAndSuperAdmins);

                    return WarehouseUtils.getResponseEntity("Super-admin created successfully!", HttpStatus.OK);
                } else {
                    return WarehouseUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return WarehouseUtils.getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            logger.error("Error creating super admin: {}", ex.getMessage());
        }
        return WarehouseUtils.getResponseEntity("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<String> getAllAdminsAndSuperAdmins() {
        try {
            // Get all admin emails
            List<String> adminEmails = userRepository.getAllAdmin();

            // Get all super-admin emails
            List<String> superAdminEmails = userRepository.findEmailsByRole("super-admin");

            // Combine both lists and remove duplicates
            Set<String> allAdmins = new HashSet<>();
            allAdmins.addAll(adminEmails);
            allAdmins.addAll(superAdminEmails);

            // Remove current user from the list
            allAdmins.remove(jwtFilter.getCurrentUser());

            return new ArrayList<>(allAdmins);
        } catch (Exception ex) {
            logger.error("Error getting all admins and super-admins: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    private void sendStatusUpdateNotification(String userEmail, String oldStatus, String newStatus, List<String> recipients) {
        try {
            if (recipients.isEmpty()) {
                logger.info("No recipients found for status update notification");
                return;
            }

            String statusAction = "true".equals(newStatus) ? "activated" : "deactivated";
            String subject = "User Status Updated - Warehouse Management System";
            String message = "User Status Update Notification\n\n" +
                    "User: " + userEmail + "\n" +
                    "Status changed from: " + ("true".equals(oldStatus) ? "Active" : "Inactive") +
                    " to: " + ("true".equals(newStatus) ? "Active" : "Inactive") + "\n" +
                    "Action: " + statusAction + "\n" +
                    "Updated by: " + jwtFilter.getCurrentUser() + "\n" +
                    "Date: " + new Date() + "\n\n" +
                    "This is an automated notification from Warehouse Management System.";

            emailUtils.simpleMailMessage(jwtFilter.getCurrentUser(), subject, message, recipients);
            logger.info("Status update notification sent for user: {} to {} recipients", userEmail, recipients.size());
        } catch (Exception ex) {
            logger.error("Error sending status update notification: {}", ex.getMessage());
        }
    }

    private void sendRoleUpdateNotification(String userEmail, String oldRole, String newRole, List<String> recipients) {
        try {
            if (recipients.isEmpty()) {
                logger.info("No recipients found for role update notification");
                return;
            }

            String subject = "User Role Updated - Warehouse Management System";
            String message = "User Role Update Notification\n\n" +
                    "User: " + userEmail + "\n" +
                    "Role changed from: " + oldRole + " to: " + newRole + "\n" +
                    "Updated by: " + jwtFilter.getCurrentUser() + "\n" +
                    "Date: " + new Date() + "\n\n" +
                    "This is an automated notification from Warehouse Management System.";

            emailUtils.simpleMailMessage(jwtFilter.getCurrentUser(), subject, message, recipients);
            logger.info("Role update notification sent for user: {} to {} recipients", userEmail, recipients.size());
        } catch (Exception ex) {
            logger.error("Error sending role update notification: {}", ex.getMessage());
        }
    }

    private void sendNewSuperAdminNotification(String userEmail, String userName, List<String> recipients) {
        try {
            if (recipients.isEmpty()) {
                logger.info("No recipients found for new super-admin notification");
                return;
            }

            String subject = "New Super Admin Created - Warehouse Management System";
            String message = "New Super Admin Notification\n\n" +
                    "A new Super Admin has been created:\n" +
                    "Name: " + userName + "\n" +
                    "Email: " + userEmail + "\n" +
                    "Created by: " + jwtFilter.getCurrentUser() + "\n" +
                    "Date: " + new Date() + "\n\n" +
                    "This is an automated notification from Warehouse Management System.";

            emailUtils.simpleMailMessage(jwtFilter.getCurrentUser(), subject, message, recipients);
            logger.info("New super-admin notification sent for user: {} to {} recipients", userEmail, recipients.size());
        } catch (Exception ex) {
            logger.error("Error sending new super-admin notification: {}", ex.getMessage());
        }
    }

    private boolean validateSignupMap(Map<String, String> requestMap) {
        return requestMap.containsKey("fullName") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setFullName(requestMap.get("fullName"));
        user.setContactNUmber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("staff");
        return user;
    }

    private boolean canModifyUser(User targetUser) {
        String targetUserRole = targetUser.getRole();
        String currentUserEmail = jwtFilter.getCurrentUser();

        if (targetUser.getEmail().equals(currentUserEmail)) {
            logger.warn("User {} attempted self-modification", currentUserEmail);
            return false;
        }

        if (jwtFilter.isSuperAdmin()) {
            return true;
        }

        if (jwtFilter.isAdmin()) {
            boolean canModify = "staff".equals(targetUserRole);
            if (!canModify) {
                logger.warn("Admin {} attempted to modify non-staff user {}", currentUserEmail, targetUserRole);
            }
            return canModify;
        }

        return false;
    }

    private boolean isValidRoleUpdate(User targetUser, String newRole) {
        String targetUserRole = targetUser.getRole();

        if (!isValidRole(newRole)) {
            logger.warn("Invalid role attempted: {}", newRole);
            return false;
        }

        if (jwtFilter.isSuperAdmin()) {
            return true;
        }

        if (jwtFilter.isAdmin()) {
            boolean isValid = "staff".equals(targetUserRole) &&
                    ("staff".equals(newRole) || "admin".equals(newRole));
            if (!isValid) {
                logger.warn("Admin attempted invalid role change: {} -> {}", targetUserRole, newRole);
            }
            return isValid;
        }

        return false;
    }

    private boolean isValidRole(String role) {
        return "super-admin".equals(role) || "admin".equals(role) || "staff".equals(role);
    }
}