package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.WaitlistCustomerRequestDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerWaitlistMapper {

    Waitlist toEntity(WaitlistCustomerRequestDTO requestDTO);
    WaitlistCustomerRequestDTO toDto(Waitlist waitlist);
    List<WaitlistCustomerRequestDTO> toDtoList(List<Waitlist> waitlists);
}
