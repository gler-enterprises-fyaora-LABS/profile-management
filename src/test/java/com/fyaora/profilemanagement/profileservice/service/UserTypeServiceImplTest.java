package com.fyaora.profilemanagement.profileservice.service;

import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeResponseDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeStatus;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserType;
import com.fyaora.profilemanagement.profileservice.model.db.repository.UserTypeRepository;
import com.fyaora.profilemanagement.profileservice.model.mapping.UserTypeMapper;
import com.fyaora.profilemanagement.profileservice.service.impl.UserTypeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserTypeMapper userTypeMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UserTypeServiceImpl userTypeService;

    // Static method to provide test data for the parameterized test.
    private static Stream<Arguments> userTypeSuccessProvider() {
        return Stream.of(
                Arguments.of(1, "Service Provider Type Test", UserTypeEnum.SERVICE_PROVIDER),
                Arguments.of(2, "Customer Type Test", UserTypeEnum.CUSTOMER),
                Arguments.of(3, "Individual Type Test", UserTypeEnum.INDIVIDUAL),
                Arguments.of(4, "Business Type Test", UserTypeEnum.BUSINESS)
        );
    }

    @ParameterizedTest
    @MethodSource("userTypeSuccessProvider")
    @DisplayName("Test: Successfully get user type for all UserTypeEnum values")
    void testGetUserType_SuccessParameterized(int id, String description, UserTypeEnum type) {
        // Arrange
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
        assertEquals(userTypeDTO.isEnabled(), result.isEnabled());
    }

    @ParameterizedTest
    @CsvSource({
            "SERVICE_PROVIDER",
            "CUSTOMER",
            "INDIVIDUAL",
            "BUSINESS"
    })
    @DisplayName("Test: Throw UserTypeNotFoundException when user type is not found")
    void testGetUserType_NotFoundParameterized(UserTypeEnum type) {
        // Arrange
        when(userTypeRepository.findByTypeAndEnabled(type, true)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS");

        // Act & Assert
        assertThrows(UserTypeNotFoundException.class, () -> userTypeService.getUserType(type));
    }

    @Test
    @DisplayName("Test: Successfully add a new user type")
    void testAddUserType_Success() {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        UserType userType = new UserType();
        userType.setType(UserTypeEnum.INDIVIDUAL);
        userType.setDescription("Individual User Type");
        userType.setEnabled(true);

        UserType savedUserType = UserType.builder().did(1).type(UserTypeEnum.INDIVIDUAL).description("Individual User Type").enabled(true).build();

        when(userTypeRepository.findByType(UserTypeEnum.INDIVIDUAL)).thenReturn(Optional.empty());
        when(userTypeMapper.userTypeDTOToUserType(userTypeDTO)).thenReturn(userType);
        when(userTypeRepository.save(userType)).thenReturn(savedUserType);

        // Act
        UserTypeResponseDTO result = userTypeService.addUserType(userTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedUserType.getDid(), result.getId());
        assertEquals(savedUserType.getType(), result.getType());
        assertEquals(UserTypeStatus.CREATED, result.getStatus());
    }

    @Test
    @DisplayName("Test: Throw IllegalArgumentException when adding a user type that already exists")
    void testAddUserType_AlreadyExists() {
        // Arrange
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        UserType existingUserType = new UserType();
        when(userTypeRepository.findByType(UserTypeEnum.INDIVIDUAL)).thenReturn(Optional.of(existingUserType));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("The type already exists.");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userTypeService.addUserType(userTypeDTO));
    }

    @Test
    @DisplayName("Test: Throw NullPointerException when addUserType is called with a null DTO")
    void testAddUserType_NullDTO() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userTypeService.addUserType(null));
    }
}