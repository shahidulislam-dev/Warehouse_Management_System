package com.warehouse_management.controllersImpl;

import com.warehouse_management.constants.WarehouseConstant;
import com.warehouse_management.controllers.AuthController;
import com.warehouse_management.services.AuthService;
import com.warehouse_management.utils.WarehouseUtils;
import com.warehouse_management.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AuthControllerImpl implements AuthController {


    AuthService authService;
    @Autowired
    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return authService.signup(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return authService.login(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return authService.getAllUser();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return authService.update(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return authService.checkToken();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            return authService.changePassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return authService.forgotPassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> createSuperAdmin(Map<String, String> requestMap) {
        try {
            return authService.createSuperAdmin(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return WarehouseUtils.getResponseEntity(WarehouseConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
