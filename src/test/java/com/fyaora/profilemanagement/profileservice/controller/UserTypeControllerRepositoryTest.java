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
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
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

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UserTypeController userTypeController;

    private UserTypeServiceImpl userTypeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userTypeService = new UserTypeServiceImpl(userTypeRepository, userTypeMapper);
        userTypeController = new UserTypeController(userTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .build();
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER"})
    @DisplayName("Test: Successfully get user type for SERVICE_PROVIDER and CUSTOMER via HTTP")
    void testGetUserTypeByType_Success_Http(UserTypeEnum type) throws Exception {
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
    @DisplayName("Integration Test: Handle 400 error when user type is missing (null)")
    void testGetUserTypeByType_NullInput() throws Exception {
        mockMvc.perform(get("/api/v1/user-type"))  // Accessing without a type param
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("User type cannot be null."))
                .andExpect(jsonPath("$.path").value("/api/v1/user-type"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}
