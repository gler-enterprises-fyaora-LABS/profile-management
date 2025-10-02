package com.fyaora.profilemanagement.profileservice.model.db.mapper;

import com.fyaora.profilemanagement.profileservice.model.request.ServiceOfferedRequest;
import com.fyaora.profilemanagement.profileservice.model.request.ServiceProviderWaitlist;
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

    Waitlist toEntity(ServiceProviderWaitlist requestDTO);

    @Mapping(target = "services", source = "waitlistServiceOffered", qualifiedByName = "mapToServiceOfferedDTOs")
    ServiceProviderWaitlist toDto(Waitlist waitlist);

    List<ServiceProviderWaitlist> toDtoList(List<Waitlist> waitlists);

    ServiceOfferedRequest toDto(ServiceOffered serviceOffered);

    @Named("mapToServiceOfferedDTOs")
    default List<ServiceOfferedRequest> mapToServiceOfferedDTOs(List<WaitlistServiceOffered> serviceLinks) {
        if (serviceLinks == null) return new ArrayList<>();
        return serviceLinks.stream()
                .map(WaitlistServiceOffered::getService)
                .map(this::toDto)
                .toList();
    }
}
