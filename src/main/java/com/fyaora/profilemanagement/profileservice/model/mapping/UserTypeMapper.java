package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper interface for converting UserType to UserTypeDTO
 */
@Mapper(componentModel = "spring")
public interface UserTypeMapper {

    @Mappings({
            @Mapping(source = "did", target = "id"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "enabled", target = "enabled")
    })
    UserTypeDTO userTypeToUserTypeDTO(UserType usertype);

    @Mappings({
            @Mapping(source = "id", target = "did"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "enabled", target = "enabled")
    })
    UserType userTypeDTOToUserType(UserTypeDTO userTypeDTO);
}
