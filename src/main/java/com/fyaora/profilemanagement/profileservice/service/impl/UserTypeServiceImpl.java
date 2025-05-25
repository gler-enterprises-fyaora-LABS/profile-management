package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.mapping.UserTypeMapper;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final UserTypeMapper userTypeMapper;

    @Autowired
    public UserTypeServiceImpl(UserTypeRepository userTypeRepository, UserTypeMapper userTypeMapper) {
        this.userTypeRepository = userTypeRepository;
        this.userTypeMapper = userTypeMapper;
    }


    public UserTypeDTO getUserType(UserTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("User type cannot be null.");
        }
        UserType userType = userTypeRepository.findByTypeAndEnabled(type, Boolean.TRUE)
                .orElseThrow(() -> new UserTypeNotFoundException("Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS"));

        return userTypeMapper.userTypeToUserTypeDTO(userType);
    }


    public UserTypeResponseDTO addUserType(UserTypeDTO userTypeDTO) {
        // Validate input
        if (userTypeDTO == null || userTypeDTO.getType() == null) {
            throw new IllegalArgumentException("User type and type enum cannot be null.");
        }

        // Check if type already exists
        if (userTypeRepository.findByType(userTypeDTO.getType()).isPresent()) {
            throw new IllegalArgumentException("The type already exists");
        }

        // Convert DTO to entity
        UserType userType = userTypeMapper.userTypeDTOToUserType(userTypeDTO);
        // Ensure enabled is set to true if not provided
        if (userType.getEnabled() == null) {
            userType.setEnabled(true);
        }

        // Save to repository
        UserType savedUserType = userTypeRepository.save(userType);

        // Create response DTO
        return new UserTypeResponseDTO(savedUserType.getDid(), savedUserType.getType(), "CREATED");
    }
}
