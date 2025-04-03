package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;

import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user-type")
public class UserTypeController {

    private final UserTypeServiceImpl userTypeServiceImpl;
    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeServiceImpl userTypeServiceImpl, UserTypeService userTypeService) {
        this.userTypeServiceImpl = userTypeServiceImpl;
        this.userTypeService = userTypeService;
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getUserTypeByType(@PathVariable Integer type) {
        UserTypeDTO userTypeDTO = userTypeService.findByType(type);
        return ResponseEntity.ok(userTypeDTO);
    }
}
