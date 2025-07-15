package com.fyaora.profilemanagement.profileservice.controller;
import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTypeControllerTest {

    @Mock
    private UserTypeService userTypeService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UserTypeController userTypeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .build();
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER"})
    @DisplayName("Test: Successfully get user type for SERVICE_PROVIDER and CUSTOMER via HTTP")
    void testGetUserTypeByType_Success_Http(UserTypeEnum type) throws Exception {
        // Arrange
        UserTypeDTO mockUserTypeDTO = new UserTypeDTO();
        mockUserTypeDTO.setId(type == UserTypeEnum.SERVICE_PROVIDER ? 1 : 2);
        mockUserTypeDTO.setType(type);
        mockUserTypeDTO.setDescription(type == UserTypeEnum.SERVICE_PROVIDER ? "Service Provider Type Test" : "Registration Service Provider Type Test");
        mockUserTypeDTO.setEnabled(true);

        when(userTypeService.getUserType(type)).thenReturn(mockUserTypeDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserTypeDTO.getId()))
                .andExpect(jsonPath("$.type").value(type.toString()))
                .andExpect(jsonPath("$.description").value(mockUserTypeDTO.getDescription()))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @DisplayName("Test: Handle UserTypeNotFoundException when user type not found")
    void testGetUserTypeByType_NotFound() throws Exception {
        // Arrange
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;
        String errorMessage = "Invalid user type provided.";
        when(userTypeService.getUserType(type)).thenThrow(new UserTypeNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.path").value("/api/v1/user-type/" + type))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type is null")
    void testGetUserTypeByType_NullInput() throws Exception {
        // Arrange
        String errorMessage = "User type cannot be null.";
        // Mock the service to throw IllegalArgumentException when type is null
        when(userTypeService.getUserType(null)).thenThrow(new IllegalArgumentException(errorMessage));

        // Act & Assert
        // Verify that accessing /api/v1/user-type returns MessageDTO with 'User type cannot be null.'
        mockMvc.perform(get("/api/v1/user-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.path").value("/api/v1/user-type"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}

