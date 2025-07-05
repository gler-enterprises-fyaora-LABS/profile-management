package com.fyaora.profilemanagement.profileservice.service.impl;

import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeStatus;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.mapping.UserTypeMapper;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final UserTypeMapper userTypeMapper;
    private final MessageSource messageSource;

    @Autowired
    public UserTypeServiceImpl(UserTypeRepository userTypeRepository, UserTypeMapper userTypeMapper, MessageSource messageSource) {
        this.userTypeRepository = userTypeRepository;
        this.userTypeMapper = userTypeMapper;
        this.messageSource = messageSource;
    }

    public UserTypeDTO getUserType(UserTypeEnum type) {
        UserType userType = userTypeRepository.findByTypeAndEnabled(type, true)
                .orElseThrow(() -> new UserTypeNotFoundException(messageSource.getMessage("invalid.enum.type", null, LocaleContextHolder.getLocale())));

        return userTypeMapper.userTypeToUserTypeDTO(userType);
    }

    public UserTypeResponseDTO addUserType(UserTypeDTO userTypeDTO) {
        // Check if type already exists
        if (userTypeRepository.findByType(userTypeDTO.getType()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("user.type.exists", null, LocaleContextHolder.getLocale()));
        }

        // Convert DTO to entity
        UserType userType = userTypeMapper.userTypeDTOToUserType(userTypeDTO);

        // Save to repository
        UserType savedUserType = userTypeRepository.save(userType);

        // Create response DTO
        return new UserTypeResponseDTO(savedUserType.getDid(), savedUserType.getType(), UserTypeStatus.CREATED);
    }
}