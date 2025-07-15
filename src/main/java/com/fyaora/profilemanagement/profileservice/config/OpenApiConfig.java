package com.fyaora.profilemanagement.profileservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Fyaora User Profile Microservice", version = "1.0", description = "Fyaora User Profile Service"))
public class OpenApiConfig {
}
