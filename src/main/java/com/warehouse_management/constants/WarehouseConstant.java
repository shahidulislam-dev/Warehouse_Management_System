package com.warehouse_management.constants;

import java.util.List;

public class WarehouseConstant {
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String INVALID_DATA = "Invalid Data.";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access.";
    public static final String STORE_LOCATION = "C:\\Users\\LENOVO X1 Carbon\\OneDrive\\Documents\\Cafe Management System\\cafe_stored_files";
    public static final List<String> EXCLUDE_URLS = List.of(
            "/api/auth/login",
            "/api/auth/forgotPassword",
            "/api/auth/signup"
    );

}
