package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;

import java.util.Optional;

public interface UserTypeService {
    Optional<UserTypeDTO> findByType(Integer type);
}
