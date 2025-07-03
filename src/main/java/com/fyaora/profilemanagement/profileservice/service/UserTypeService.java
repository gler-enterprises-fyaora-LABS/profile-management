package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;

public interface UserTypeService {
    UserTypeDTO getUserType(UserTypeEnum type);
    UserTypeResponseDTO addUserType(UserTypeDTO userTypeDTO);
}
