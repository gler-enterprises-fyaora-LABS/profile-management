package com.fyaora.profilemanagement.profileservice.model.db.mapper;

import com.fyaora.profilemanagement.profileservice.model.response.CustomerWaitlist;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerWaitlistMapper {

    Waitlist toEntity(CustomerWaitlist requestDTO);
    CustomerWaitlist toDto(Waitlist waitlist);
    List<CustomerWaitlist> toDtoList(List<Waitlist> waitlists);
}
