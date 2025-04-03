package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;

import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/user-type")
public class UserTypeController {

    private final UserTypeServiceImpl userTypeServiceImpl;

    public UserTypeController(UserTypeServiceImpl userTypeServiceImpl) {
        this.userTypeServiceImpl = userTypeServiceImpl;
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getUserTypeByType(@PathVariable Integer type) {
        Optional<UserTypeDTO> userTypeDTO = userTypeServiceImpl.findByType(type);

        // Check if the userType is present, else return badRequest with message
        if (userTypeDTO.isPresent()) {
            return ResponseEntity.ok(userTypeDTO.get()); // Return the DTO if found
        } else {
            return ResponseEntity.badRequest().body("Invalid user type provided."); // Return bad request if not found
        }
    }
}
