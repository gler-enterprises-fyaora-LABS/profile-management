package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.ServiceOfferedDTO;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistServiceProviderRequestDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import com.fyaora.profilemanagement.profileservice.model.db.entity.WaitlistServiceOffered;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceProviderWaitlistMapper {

    Waitlist toEntity(WaitlistServiceProviderRequestDTO requestDTO);

    @Mapping(target = "services", source = "waitlistServiceOffered", qualifiedByName = "mapToServiceOfferedDTOs")
    WaitlistServiceProviderRequestDTO toDto(Waitlist waitlist);

    List<WaitlistServiceProviderRequestDTO> toDtoList(List<Waitlist> waitlists);

    ServiceOfferedDTO toDto(ServiceOffered serviceOffered);

    @Named("mapToServiceOfferedDTOs")
    default List<ServiceOfferedDTO> mapToServiceOfferedDTOs(List<WaitlistServiceOffered> serviceLinks) {
        if (serviceLinks == null) return new ArrayList<>();
        return serviceLinks.stream()
                .map(WaitlistServiceOffered::getService)
                .map(this::toDto)
                .toList();
    }
}
