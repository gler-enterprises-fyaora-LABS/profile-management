package com.fyaora.profilemanagement.profileservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.advice.InvalidEnumValueException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;

public class UserTypeEnumDeserializer extends JsonDeserializer<UserTypeEnum> {

    private final MessageSource messageSource;

    public UserTypeEnumDeserializer(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public UserTypeEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String input = p.getText();

        // Normalize input: uppercase, remove spaces, dashes, underscores
        String normalized = input.trim()
                .toUpperCase()
                .replaceAll("[ _-]", "");

        switch (normalized) {
            case "BUSINESS":
                return UserTypeEnum.BUSINESS;
            case "INDIVIDUAL":
                return UserTypeEnum.INDIVIDUAL;
            case "SERVICE_PROVIDER":
                return UserTypeEnum.SERVICE_PROVIDER;
            case "CUSTOMER":
                return UserTypeEnum.CUSTOMER;
            default:
                throw new InvalidEnumValueException(
                        messageSource.getMessage("invalid.enum.type", null, LocaleContextHolder.getLocale())
                );
        }
    }
}