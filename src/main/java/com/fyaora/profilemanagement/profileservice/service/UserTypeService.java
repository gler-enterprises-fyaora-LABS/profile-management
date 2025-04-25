package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;

public interface UserTypeService {
    UserTypeDTO getUserType(UserTypeEnum type);
}
