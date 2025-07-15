package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfferedMapper {

    ServiceOffered toEntity(ServiceOfferedDTO serviceDTO);
    ServiceOfferedDTO toDto(ServiceOffered service);
    List<ServiceOfferedDTO> toDtoList(List<ServiceOffered> serviceOffereds);
}
