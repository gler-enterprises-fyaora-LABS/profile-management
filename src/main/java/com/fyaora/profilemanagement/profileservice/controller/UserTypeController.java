package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.model.response.UserTypeDTO;

import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-type")
public class UserTypeController {

    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getUserTypeByType(@PathVariable UserTypeEnum type) {
        UserTypeDTO userTypeDTO = userTypeService.getUserType(type);
        return ResponseEntity.ok(userTypeDTO);
    }

    @GetMapping
    public ResponseEntity<?> getUserTypeByType() {
        UserTypeDTO userTypeDTO = userTypeService.getUserType(null);
        return ResponseEntity.ok(userTypeDTO);
    }
}