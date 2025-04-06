package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)  // Import your exception handler
class UserTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc for sending requests to the controller

    @MockitoBean
    private UserTypeService userTypeService; // Mocked service

    @Test
    @DisplayName("Integration Test: Successfully get user type for REGISTRATION_SERVICE_PROVIDER")
    void testGetUserTypeByType_RegistrationServiceProvider() throws Exception {
        UserTypeEnum type = UserTypeEnum.REGISTRATION_SERVICE_PROVIDER;
        UserTypeDTO mockUserTypeDTO = new UserTypeDTO();
        mockUserTypeDTO.setType(type);
        mockUserTypeDTO.setDescription("Service Provider Type Test");
        mockUserTypeDTO.setEnabled(true);

        // Mock the service method
        when(userTypeService.findByType(type)).thenReturn(mockUserTypeDTO);

        // Perform GET request and verify the result
        mockMvc.perform(get("/api/v1/user-type/{type}", type))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"type\":\"REGISTRATION_SERVICE_PROVIDER\",\"description\":\"Service Provider Type Test\",\"enabled\":true}"));
    }

    @Test
    @DisplayName("Integration Test: Handle null response body")
    void testGetUserTypeByType_NullResponseBody() throws Exception {
        UserTypeEnum type = UserTypeEnum.REGISTRATION_SERVICE_PROVIDER;

        // Mock the service method to return null
        when(userTypeService.findByType(type)).thenReturn(null);

        // Perform GET request and verify the result
        mockMvc.perform(get("/api/v1/user-type/{type}", type))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Integration Test: Handle UserTypeNotFoundException when user type not found")
    void testGetUserTypeByType_NotFound() throws Exception {
        UserTypeEnum type = UserTypeEnum.REGISTRATION_SERVICE_PROVIDER;

        // Mock the service to throw UserTypeNotFoundException
        when(userTypeService.findByType(type)).thenThrow(new UserTypeNotFoundException("Invalid user type provided."));

        // Perform GET request and verify the exception handling
        mockMvc.perform(get("/api/v1/user-type/{type}", type))
                .andExpect(status().isBadRequest())  // Expecting 400 status due to global exception handler
                .andExpect(content().string("Invalid user type provided."));
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
