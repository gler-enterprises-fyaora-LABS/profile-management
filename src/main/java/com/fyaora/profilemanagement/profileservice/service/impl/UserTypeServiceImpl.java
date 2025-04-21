package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
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


    public UserTypeDTO findByType(UserTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("User type cannot be null.");
        }
        UserType userType = userTypeRepository.findByTypeAndEnabled(type, Boolean.TRUE)
                .orElseThrow(() -> new UserTypeNotFoundException("Invalid user type provided."));

        return userTypeMapper.userTypeToUserTypeDTO(userType);
    }
}
