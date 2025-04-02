package com.fyaora.typeprovider.typeprovider.service;

import com.fyaora.typeprovider.typeprovider.dto.UserTypeDTO;

import java.util.Optional;

public interface UserTypeService {
    Optional<UserTypeDTO> findByType(Integer type);
}
