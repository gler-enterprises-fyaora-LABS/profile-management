package com.fyaora.profilemanagement.profileservice.model.db.mapper;

import com.fyaora.profilemanagement.profileservice.model.request.InvestorWaitlist;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestorWaitlistMapper {

    Waitlist toEntity(InvestorWaitlist requestDTO);
    InvestorWaitlist toDto(Waitlist waitlist);
    List<InvestorWaitlist> toDtoList(List<Waitlist> waitlists);
}
