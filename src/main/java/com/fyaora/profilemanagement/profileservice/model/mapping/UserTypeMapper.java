package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting UserType to UserTypeDTO
 */
@Mapper(componentModel = "spring")
public interface UserTypeMapper {

    UserTypeDTO userTypeToUserTypeDTO(UserType usertype);
    UserType userTypeDTOToUserType(UserTypeDTO userTypeDTO);
}
