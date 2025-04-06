package com.fyaora.profilemanagement.profileservice.model.db.mapping;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

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
}
