package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTypeServiceImpl {

    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTypeServiceImpl(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public Optional<UserTypeDTO> findByType(Integer type) {
        Optional<UserType> userType = userTypeRepository.findById(type);
        return userType.map(this::convertToDTO); // Convert to DTO before returning
    }

    private UserTypeDTO convertToDTO(UserType userType) {
        return new UserTypeDTO(userType.getType(), userType.getDescription(), userType.getEnabled());
    }
}
