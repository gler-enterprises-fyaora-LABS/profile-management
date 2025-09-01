package com.fyaora.profilemanagement.profileservice.model.db.entity.converters;

import com.fyaora.profilemanagement.profileservice.model.enums.VendorTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VendorTypeConverter implements AttributeConverter<VendorTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(VendorTypeEnum attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }

    @Override
    public VendorTypeEnum convertToEntityAttribute(Integer dbData) {
        return (dbData == null) ? null : VendorTypeEnum.fromCode(dbData);
    }
}
