package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fyaora.profilemanagement.profileservice.dto.deserializer.UserTypeEnumDeserializer;

@JsonDeserialize(using = UserTypeEnumDeserializer.class)
public enum UserTypeEnum {
    BUSINESS,
    INDIVIDUAL,
    SERVICE_PROVIDER,
    CUSTOMER
}