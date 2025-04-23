package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.UserTypeMapper;
import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class UserTypeControllerIntegrationTest {

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
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER"})
    @DisplayName("Test: Successfully get user type for SERVICE_PROVIDER and CUSTOMER via HTTP")
    void testGetUserTypeByType_Success_Http(UserTypeEnum type) throws Exception {
        // Arrange
        UserType userType = UserType.builder()
                .did(type == UserTypeEnum.SERVICE_PROVIDER ? 1 : 2)
                .type(type)
                .description(type == UserTypeEnum.SERVICE_PROVIDER ? "Service Provider Type Test" : "Customer Type Test")
                .enabled(true)
                .build();

        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(userType.getDid());
        userTypeDTO.setType(type);
        userTypeDTO.setDescription(userType.getDescription());
        userTypeDTO.setEnabled(userType.getEnabled());

        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.of(userType));
        when(userTypeMapper.userTypeToUserTypeDTO(userType)).thenReturn(userTypeDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format(
                        "{\"id\":%d,\"type\":\"%s\",\"description\":\"%s\",\"enabled\":true}",
                        userTypeDTO.getId(),
                        type.toString(),
                        userTypeDTO.getDescription()
                )));
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"CUSTOMER", "SERVICE_PROVIDER"})
    @DisplayName("Test: Handle UserTypeNotFoundException for all invalid user types")
    void testGetUserTypeByType_NotFound_Http(UserTypeEnum type) throws Exception {
        // Arrange
        String errorMessage = "Invalid user type provided.";
        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.path").value("/api/v1/user-type/" + type))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Integration Test: Handle UserTypeNotFoundException when user type not found")
    void testGetUserTypeByType_NotFound() throws Exception {
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;

        // Mock the service to throw UserTypeNotFoundException
        when(userTypeService.getUserType(type)).thenThrow(new UserTypeNotFoundException("Invalid user type provided."));

        // Perform GET request and verify the exception handling
        mockMvc.perform(get("/api/v1/user-type/{type}", type))
                .andExpect(status().isNotFound())  // Expecting 404 now
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Invalid user type provided."))
                .andExpect(jsonPath("$.path").value("/api/v1/user-type/" + type.name()));
    }

    @Test
    @DisplayName("Integration Test: Handle 404 error when user type is null")
    void testGetUserTypeByType_NullInput() throws Exception {
        String invalidType = null;

        // Perform GET request with invalid (null) type
        mockMvc.perform(get("/api/v1/user-type/{type}", invalidType))  // type is null. i.e: When accessing /api/v1/user-type/, expect a 404 not found.
                .andExpect(status().isNotFound())  // Expecting 404 status from global exception handler
                .andExpect(content().string(""));  //
    }

}
