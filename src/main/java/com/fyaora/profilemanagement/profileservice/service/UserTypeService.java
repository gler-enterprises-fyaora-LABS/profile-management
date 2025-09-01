package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.model.response.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;

public interface UserTypeService {
    UserTypeDTO getUserType(UserTypeEnum type);
}
