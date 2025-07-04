package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeStatus;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTypeControllerTest {

    @Mock
    private UserTypeService userTypeService;

    @InjectMocks
    private UserTypeController userTypeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
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
        mockUserTypeDTO.setDescription(type == UserTypeEnum.SERVICE_PROVIDER ? "Service Provider Type Test" : "Customer Type Test");
        mockUserTypeDTO.setEnabled(true);

        when(userTypeService.getUserType(type)).thenReturn(mockUserTypeDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type/{type}", type)
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
        String errorMessage = "Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS";
        when(userTypeService.getUserType(type)).thenThrow(new UserTypeNotFoundException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type is null")
    void testGetUserTypeByType_NullInput() throws Exception {
        // Arrange
        String errorMessage = "User type cannot be null.";
        when(userTypeService.getUserType(null)).thenThrow(new IllegalArgumentException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("Test: Successfully add a new user type via HTTP")
    void testAddUserType_Success() throws Exception {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        UserTypeResponseDTO responseDTO = new UserTypeResponseDTO(1, UserTypeEnum.INDIVIDUAL, UserTypeStatus.CREATED);

        when(userTypeService.addUserType(any(UserTypeDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.type").value(responseDTO.getType().toString()))
                .andExpect(jsonPath("$.status").value(responseDTO.getStatus().toString()));
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type DTO is null")
    void testAddUserType_NullInput() throws Exception {
        // Arrange
        String errorMessage = "Bad request";
        // No stubbing needed as JSON deserialization will fail

        // Act & Assert
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type enum is null")
    void testAddUserType_NullTypeEnum() throws Exception {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(null);
        userTypeDTO.setDescription("Invalid Type");
        userTypeDTO.setEnabled(true);

        String errorMessage = "User type and type enum cannot be null.";
        when(userTypeService.addUserType(any(UserTypeDTO.class))).thenThrow(new IllegalArgumentException(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type already exists")
    void testAddUserType_AlreadyExists() throws Exception {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        String errorMessage = "The type already exists";
        when(userTypeService.addUserType(any(UserTypeDTO.class))).thenThrow(new IllegalArgumentException(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}