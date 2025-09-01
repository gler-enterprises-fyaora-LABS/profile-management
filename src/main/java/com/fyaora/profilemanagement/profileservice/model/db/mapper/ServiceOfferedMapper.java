package com.fyaora.profilemanagement.profileservice.model.db.mapper;

import com.fyaora.profilemanagement.profileservice.model.request.ServiceOfferedRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOfferedMapper {

    ServiceOffered toEntity(ServiceOfferedRequest serviceDTO);
    ServiceOfferedRequest toDto(ServiceOffered service);
    List<ServiceOfferedRequest> toDtoList(List<ServiceOffered> serviceOffereds);
}
