package com.fyaora.profilemanagement.profileservice.model.response.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fyaora.profilemanagement.profileservice.model.enums.VendorTypeEnum;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;

public class VendorTypeEnumDeserializer extends JsonDeserializer<VendorTypeEnum> {

    private final MessageSource messageSource;

    public VendorTypeEnumDeserializer(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public VendorTypeEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().trim();
        String message = messageSource.getMessage("vendor.type.notnull", null, LocaleContextHolder.getLocale());
        String messageWithValue =  messageSource.getMessage("vendor.type.invalid", new Object[]{value}, LocaleContextHolder.getLocale());

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        try {
            return VendorTypeEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(messageWithValue);
        }
    }

}
