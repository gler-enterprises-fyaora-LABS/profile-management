package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.UserTypeMapper;
import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserTypeControllerRepositoryTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserTypeMapper userTypeMapper;

    @InjectMocks
    private UserTypeController userTypeController;

    private UserTypeServiceImpl userTypeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userTypeService = new UserTypeServiceImpl(userTypeRepository, userTypeMapper);
        userTypeController = new UserTypeController(userTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER", "INDIVIDUAL", "BUSINESS"})
    @DisplayName("Test: Successfully get user type for all UserTypeEnum values via HTTP")
    void testGetUserTypeByType_Success_Http(UserTypeEnum type) throws Exception {
        // Arrange
        int id;
        String description;
        switch (type) {
            case SERVICE_PROVIDER:
                id = 1;
                description = "Service Provider Type Test";
                break;
            case CUSTOMER:
                id = 2;
                description = "Customer Type Test";
                break;
            case INDIVIDUAL:
                id = 3;
                description = "Individual Type Test";
                break;
            case BUSINESS:
                id = 4;
                description = "Business Type Test";
                break;
            default:
                throw new IllegalArgumentException("Unknown UserTypeEnum: " + type);
        }

        UserType userType = UserType.builder()
                .did(id)
                .type(type)
                .description(description)
                .enabled(true)
                .build();

        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(id);
        userTypeDTO.setType(type);
        userTypeDTO.setDescription(description);
        userTypeDTO.setEnabled(true);

        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.of(userType));
        when(userTypeMapper.userTypeToUserTypeDTO(userType)).thenReturn(userTypeDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userTypeDTO.getId()))
                .andExpect(jsonPath("$.type").value(type.toString()))
                .andExpect(jsonPath("$.description").value(userTypeDTO.getDescription()))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER", "INDIVIDUAL", "BUSINESS"})
    @DisplayName("Test: Handle UserTypeNotFoundException for all user types not found")
    void testGetUserTypeByType_NotFound_Http(UserTypeEnum type) throws Exception {
        // Arrange
        String errorMessage = "Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS";
        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

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

        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}