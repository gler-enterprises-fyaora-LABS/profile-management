package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.WaitlistInvestorRequestDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestorWaitlistMapper {

    Waitlist toEntity(WaitlistInvestorRequestDTO requestDTO);
    WaitlistInvestorRequestDTO toDto(Waitlist waitlist);
    List<WaitlistInvestorRequestDTO> toDtoList(List<Waitlist> waitlists);
}
