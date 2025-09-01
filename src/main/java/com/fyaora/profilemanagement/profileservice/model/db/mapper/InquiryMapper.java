package com.fyaora.profilemanagement.profileservice.model.db.mapper;

import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InquiryMapper {

    Inquiry toEntity(InquiryRequest inquiryRequest);
    InquiryRequest toDto(Inquiry inquiry);
    List<InquiryRequest> toDtoList(List<Inquiry> inquiries);
}
