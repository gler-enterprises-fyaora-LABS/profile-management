package com.fyaora.profilemanagement.profileservice.service;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
                () -> userTypeService.getUserType(null)
        );
        assertEquals("User type cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(UserTypeEnum.class)
    @DisplayName("Test: Throw UserTypeNotFoundException when UserType is not found")
    void testFindByType_NotFoundParameterized(UserTypeEnum type) {
        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.getUserType(type)
        );

        assertEquals("Invalid user type provided.", exception.getMessage());
    }
}