package com.fyaora.profilemanagement.profileservice.model.db.mapper;

import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InquiryMapper {

    Inquiry toEntity(InquiryRequest inquiryRequest);
    InquiryRequest toDto(Inquiry inquiry);

    default List<InquiryRequest> toDtoList(List<Inquiry> inquiries) {
        if (null == inquiries || inquiries.isEmpty()) return List.of();

        return inquiries.stream()
                .map(inquiry -> InquiryRequest.builder()
                        .id(inquiry.getId())
                        .firstName(inquiry.getFirstName())
                        .lastName(inquiry.getLastName())
                        .email(inquiry.getEmail())
                        .message(truncateMessage(inquiry))
                        .build()
                ).collect(Collectors.toList());
    };

    private static String truncateMessage(Inquiry inquiry) {
        if (StringUtils.isNotBlank(inquiry.getMessage()) && inquiry.getMessage().length() > 20) {
            return inquiry.getMessage().substring(0, 20).concat("...");
        }
        return inquiry.getMessage();
    }
}
