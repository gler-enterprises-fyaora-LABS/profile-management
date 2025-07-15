package com.fyaora.profilemanagement.profileservice.model.db.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeEnumConverter implements AttributeConverter<UserTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }

    @Override
    public UserTypeEnum convertToEntityAttribute(Integer dbData) {
        return (dbData == null) ? null : UserTypeEnum.fromCode(dbData);
    }
}
