package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account-type")
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

    @PostMapping
    public ResponseEntity<?> addUserType(@RequestBody UserTypeDTO userTypeDTO) {
        UserTypeResponseDTO responseDTO = userTypeService.addUserType(userTypeDTO);
        return ResponseEntity.ok(responseDTO);
    }
}