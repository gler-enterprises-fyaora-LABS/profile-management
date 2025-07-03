package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER", "INDIVIDUAL", "BUSINESS"})
    @DisplayName("Test: Successfully get user type for all UserTypeEnum values")
    void testGetUserType_SuccessParameterized(UserTypeEnum type) {
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

        // Act
        UserTypeDTO result = userTypeService.getUserType(type);

        // Assert
        assertEquals(userTypeDTO.getId(), result.getId());
        assertEquals(userTypeDTO.getType(), result.getType());
        assertEquals(userTypeDTO.getDescription(), result.getDescription());
        assertEquals(userTypeDTO.getEnabled(), result.getEnabled());
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER", "INDIVIDUAL", "BUSINESS"})
    @DisplayName("Test: Throw UserTypeNotFoundException when user type is not found")
    void testGetUserType_NotFoundParameterized(UserTypeEnum type) {
        // Arrange
        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

        // Act & Assert
        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.getUserType(type)
        );
        assertEquals("Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER", "INDIVIDUAL", "BUSINESS"})
    @DisplayName("Test: Throw UserTypeNotFoundException when user type is disabled")
    void testGetUserType_DisabledParameterized(UserTypeEnum type) {
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
                .enabled(false)
                .build();

        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());

        // Act & Assert
        UserTypeNotFoundException exception = assertThrows(
                UserTypeNotFoundException.class,
                () -> userTypeService.getUserType(type)
        );
        assertEquals("Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw exception when user type is null")
    void testGetUserType_NullInput() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userTypeService.getUserType(null)
        );
        assertEquals("User type cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Successfully add a new user type")
    void testAddUserType_Success() {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        UserType userType = UserType.builder()
                .did(1)
                .type(UserTypeEnum.INDIVIDUAL)
                .description("Individual User Type")
                .enabled(true)
                .build();

        UserTypeResponseDTO responseDTO = new UserTypeResponseDTO(1, UserTypeEnum.INDIVIDUAL, "CREATED");

        when(userTypeRepository.findByType(UserTypeEnum.INDIVIDUAL)).thenReturn(Optional.empty());
        when(userTypeMapper.userTypeDTOToUserType(userTypeDTO)).thenReturn(userType);
        when(userTypeRepository.save(userType)).thenReturn(userType);

        // Act
        UserTypeResponseDTO result = userTypeService.addUserType(userTypeDTO);

        // Assert
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getType(), result.getType());
        assertEquals(responseDTO.getStatus(), result.getStatus());
    }

    @Test
    @DisplayName("Test: Throw IllegalArgumentException when user type DTO is null")
    void testAddUserType_NullDTO() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userTypeService.addUserType(null)
        );
        assertEquals("User type and type enum cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw IllegalArgumentException when user type enum is null")
    void testAddUserType_NullTypeEnum() {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(null);
        userTypeDTO.setDescription("Invalid Type");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userTypeService.addUserType(userTypeDTO)
        );
        assertEquals("User type and type enum cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Throw IllegalArgumentException when user type already exists")
    void testAddUserType_AlreadyExists() {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        UserType userType = UserType.builder()
                .did(1)
                .type(UserTypeEnum.INDIVIDUAL)
                .description("Individual User Type")
                .enabled(true)
                .build();

        when(userTypeRepository.findByType(UserTypeEnum.INDIVIDUAL)).thenReturn(Optional.of(userType));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userTypeService.addUserType(userTypeDTO)
        );
        assertEquals("The type already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Successfully add user type with enabled set to null")
    void testAddUserType_EnabledNull() {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setId(1);
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(null);

        UserType userType = UserType.builder()
                .did(1)
                .type(UserTypeEnum.INDIVIDUAL)
                .description("Individual User Type")
                .enabled(null)
                .build();

        UserType savedUserType = UserType.builder()
                .did(1)
                .type(UserTypeEnum.INDIVIDUAL)
                .description("Individual User Type")
                .enabled(true)
                .build();

        UserTypeResponseDTO responseDTO = new UserTypeResponseDTO(1, UserTypeEnum.INDIVIDUAL, "CREATED");

        when(userTypeRepository.findByType(UserTypeEnum.INDIVIDUAL)).thenReturn(Optional.empty());
        when(userTypeMapper.userTypeDTOToUserType(userTypeDTO)).thenReturn(userType);
        when(userTypeRepository.save(userType)).thenReturn(savedUserType);

        // Act
        UserTypeResponseDTO result = userTypeService.addUserType(userTypeDTO);

        // Assert
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getType(), result.getType());
        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertTrue(savedUserType.getEnabled(), "Enabled should be set to true by the service");
    }
}