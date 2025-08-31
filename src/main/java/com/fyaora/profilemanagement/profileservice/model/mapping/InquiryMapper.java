package com.fyaora.profilemanagement.profileservice.model.mapping;

import com.fyaora.profilemanagement.profileservice.dto.InquiryDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InquiryMapper {

    Inquiry toEntity(InquiryDTO inquiryDTO);
    InquiryDTO toDto(Inquiry inquiry);
    List<InquiryDTO> toDtoList(List<Inquiry> inquiries);
}
