package com.fyaora.profilemanagement.profileservice.service;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.UserTypeMapper;
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

    @Mock
    private UserTypeMapper userTypeMapper;

    @InjectMocks
    private UserTypeServiceImpl userTypeService;

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER"})
    @DisplayName("Test: Successfully get user type for SERVICE_PROVIDER and CUSTOMER")
    void testGetUserType_SuccessParameterized(UserTypeEnum type) {
        // Arrange
        UserType userType = UserType.builder()
                .id(type == UserTypeEnum.SERVICE_PROVIDER ? 1 : 2)
                .type(type)
                .description(type == UserTypeEnum.SERVICE_PROVIDER ? "Service Provider Type Test" : "Customer Type Test")
                .enabled(true)
                .build();

        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(userType.getId());
        userTypeDTO.setType(type);
        userTypeDTO.setDescription(userType.getDescription());
        userTypeDTO.setEnabled(userType.getEnabled());

        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.of(userType));
        when(userTypeMapper.userTypeToUserTypeDTO(userType)).thenReturn(userTypeDTO);

        // Act
        UserTypeDTO result = userTypeService.getUserType(type);

        // Assert
        assertEquals(userTypeDTO.getId(), result.getId());
        assertEquals(userTypeDTO.getType(), result.getType());
        assertEquals(userTypeDTO.getDescription(), result.getDescription());
        assertEquals(userTypeDTO.getEnabled(), result.getEnabled());
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER"})
    @DisplayName("Test: Throw UserTypeNotFoundException when user type is not found")
    void testGetUserType_NotFoundParameterized(UserTypeEnum type) {
        // Arrange
        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

        // Act & Assert
        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.getUserType(type)
        );
        assertEquals("Invalid user type provided.", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER"})
    @DisplayName("Test: Throw UserTypeNotFoundException when user type is disabled")
    void testGetUserType_DisabledParameterized(UserTypeEnum type) {
        // Arrange
        UserType userType = UserType.builder()
                .id(type == UserTypeEnum.SERVICE_PROVIDER ? 1 : 2)
                .type(type)
                .description(type == UserTypeEnum.SERVICE_PROVIDER ? "Service Provider Type Test" : "Customer Type Test")
                .enabled(false) // Explicitly disabled
                .build();

        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

        // Act & Assert
        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.getUserType(type)
        );
        assertEquals("Invalid user type provided.", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw exception when user type is null")
    void testFindByType_NullInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userTypeService.getUserType(null)
        );
        assertEquals("User type cannot be null.", exception.getMessage());
    }

}