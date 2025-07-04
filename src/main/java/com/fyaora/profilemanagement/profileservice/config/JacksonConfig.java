package com.fyaora.profilemanagement.profileservice.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.dto.deserializer.UserTypeEnumDeserializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private final MessageSource messageSource;

    public JacksonConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    public UserTypeEnumDeserializer userTypeEnumDeserializer() {
        return new UserTypeEnumDeserializer(messageSource);
    }

    @Bean
    public Module customEnumModule(UserTypeEnumDeserializer userTypeEnumDeserializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(UserTypeEnum.class, userTypeEnumDeserializer);
        return module;
    }
}