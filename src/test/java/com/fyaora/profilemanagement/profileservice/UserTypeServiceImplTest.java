package com.fyaora.profilemanagement.profileservice;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;


    @InjectMocks
    private UserTypeServiceImpl userTypeService;

    @Test
    @DisplayName("Test: Throw exception when user type is null")
    void testFindByType_NullInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userTypeService.findByType(null)
        );
        assertEquals("User type cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw UserTypeNotFoundException when UserType is not found")
    void testFindByType_NotFound() {
        UserTypeEnum type = UserTypeEnum.REGISTRATION_SERVICE_PROVIDER;

        // Mocking the repository to return empty for the given type
        when(userTypeRepository.findByType(type)).thenReturn(Optional.empty());

        // Assert that the exception is thrown and message matches
        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.findByType(type)
        );
        assertEquals("Invalid user type provided.", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw UserTypeNotFoundException when CUSTOMER_PROVIDER is not found")
    void testFindByType_NotFound_CustomerProvider() {
        UserTypeEnum type = UserTypeEnum.CUSTOMER_PROVIDER;

        // Mocking the repository to return empty for the given type
        when(userTypeRepository.findByType(type)).thenReturn(Optional.empty());

        // Assert that the exception is thrown and message matches
        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.findByType(type)
        );
        assertEquals("Invalid user type provided.", exception.getMessage());
    }
}
