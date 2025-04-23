package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Test
    @DisplayName("Integration Test: Successfully get user type for REGISTRATION_SERVICE_PROVIDER")
    void testGetUserTypeByType_RegistrationServiceProvider() throws Exception {
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;
        UserTypeDTO mockUserTypeDTO = new UserTypeDTO();
        mockUserTypeDTO.setType(type);
        mockUserTypeDTO.setDescription("Service Provider Type Test");
        mockUserTypeDTO.setEnabled(true);

        // Mock the service method
        when(userTypeService.getUserType(type)).thenReturn(mockUserTypeDTO);

        // Perform GET request and verify the result
        mockMvc.perform(get("/api/v1/user-type/{type}", type))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"type\":\"SERVICE_PROVIDER\",\"description\":\"Service Provider Type Test\",\"enabled\":true}"));
    }

    @Test
    @DisplayName("Integration Test: Handle null response body")
    void testGetUserTypeByType_NullResponseBody() throws Exception {
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;

        // Mock the service method to return null
        when(userTypeService.getUserType(type)).thenReturn(null);

        // Perform GET request and verify the result
        mockMvc.perform(get("/api/v1/user-type/{type}", type))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
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
