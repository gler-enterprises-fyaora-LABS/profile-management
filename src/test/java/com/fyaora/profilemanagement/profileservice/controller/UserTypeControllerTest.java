package com.fyaora.profilemanagement.profileservice.controller;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeControllerTest {

    @Mock
    private UserTypeService userTypeService;


    @InjectMocks
    private UserTypeController userTypeController;

    @Test
    @DisplayName("Test: Successfully get user type for SERVICE_PROVIDER")
    void testGetUserTypeByType_RegistrationServiceProvider() {
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;
        UserTypeDTO mockUserTypeDTO = new UserTypeDTO();
        mockUserTypeDTO.setType(type);
        mockUserTypeDTO.setDescription("Service Provider Type Test");
        mockUserTypeDTO.setEnabled(true);

        // Mock the service method
        when(userTypeService.getUserType(type)).thenReturn(mockUserTypeDTO);

        // Call the controller method
        ResponseEntity<?> response = userTypeController.getUserTypeByType(type);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserTypeDTO, response.getBody());
    }

    @Test
    @DisplayName("Test: Handle null response body")
    void testGetUserTypeByType_NullResponseBody() {
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;

        // Mock the service method to return null
        when(userTypeService.getUserType(type)).thenReturn(null);

        // Call the controller method
        ResponseEntity<?> response = userTypeController.getUserTypeByType(type);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check that the response body is null or empty
        assertNull(response.getBody()); // or use assertTrue(response.getBody().isEmpty()) if expecting an empty object
    }

    @Test
    @DisplayName("Test: Handle UserTypeNotFoundException when user type not found")
    void testGetUserTypeByType_NotFound() {
        // Arrange
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;

        // Mock the service to throw the exception
        when(userTypeService.getUserType(type)).thenThrow(new UserTypeNotFoundException("Invalid user type provided."));

        // Act & Assert
        // We expect an exception to be thrown here, which will be caught by the global exception handler
        UserTypeNotFoundException thrown = assertThrows(UserTypeNotFoundException.class, () -> {
            userTypeController.getUserTypeByType(type);
        });

        // Assert the exception message
        assertEquals("Invalid user type provided.", thrown.getMessage());
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type is null")
    void testGetUserTypeByType_NullInput() {
        // Arrange
        UserTypeEnum type = null;

        // Mock the service to throw the exception when null is passed
        when(userTypeService.getUserType(type)).thenThrow(new IllegalArgumentException("User type cannot be null."));

        // Act & Assert
        // We expect an IllegalArgumentException to be thrown here
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userTypeController.getUserTypeByType(type);
        });

        // Assert the exception message
        assertEquals("User type cannot be null.", thrown.getMessage());
    }
}

