package com.fyaora.profilemanagement.profileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fyaora.profilemanagement.profileservice.advice.GlobalExceptionHandler;
import com.fyaora.profilemanagement.profileservice.advice.UserTypeNotFoundException;
import com.fyaora.profilemanagement.profileservice.dto.*;
import com.fyaora.profilemanagement.profileservice.dto.deserializer.UserTypeEnumDeserializer;
import com.fyaora.profilemanagement.profileservice.service.UserTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserTypeControllerTest {

    @Mock
    private UserTypeService userTypeService;

    @InjectMocks
    private UserTypeController userTypeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Setup message source for deserializer
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        // Configure ObjectMapper with all required modules
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Register custom deserializer
        UserTypeEnumDeserializer deserializer = new UserTypeEnumDeserializer(messageSource);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(UserTypeEnum.class, deserializer);
        objectMapper.registerModule(module);

        // Configure MockMvc with custom message converter
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private static Stream<Arguments> userTypeSuccessProvider() {
        return Stream.of(
                Arguments.of(1, "Service Provider", UserTypeEnum.SERVICE_PROVIDER),
                Arguments.of(2, "Customer", UserTypeEnum.CUSTOMER),
                Arguments.of(3, "Individual", UserTypeEnum.INDIVIDUAL),
                Arguments.of(4, "Business", UserTypeEnum.BUSINESS)
        );
    }

    @ParameterizedTest
    @MethodSource("userTypeSuccessProvider")
    @DisplayName("Test: Successfully get user type for all UserTypeEnum values via HTTP")
    void testGetUserTypeByType_Success_Http(int id, String description, UserTypeEnum type) throws Exception {
        UserTypeDTO mockUserTypeDTO = new UserTypeDTO();
        mockUserTypeDTO.setId(id);
        mockUserTypeDTO.setType(type);
        mockUserTypeDTO.setDescription(description);
        mockUserTypeDTO.setEnabled(true);

        when(userTypeService.getUserType(type)).thenReturn(mockUserTypeDTO);

        mockMvc.perform(get("/api/v1/account-type/{type}", type)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value(type.name()))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @DisplayName("Test: Handle UserTypeNotFoundException when user type not found")
    void testGetUserTypeByType_NotFound() throws Exception {
        UserTypeEnum type = UserTypeEnum.SERVICE_PROVIDER;
        String errorMessage = "Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS";
        when(userTypeService.getUserType(type)).thenThrow(new UserTypeNotFoundException(errorMessage));

        mockMvc.perform(get("/api/v1/account-type/{type}", type)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Test: Successfully add a new user type via HTTP")
    void testAddUserType_Success() throws Exception {
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        UserTypeResponseDTO responseDTO = new UserTypeResponseDTO(1, UserTypeEnum.INDIVIDUAL, UserTypeStatus.CREATED);

        when(userTypeService.addUserType(any(UserTypeDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("INDIVIDUAL"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }


    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type already exists")
    void testAddUserType_AlreadyExists() throws Exception {
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        userTypeDTO.setType(UserTypeEnum.INDIVIDUAL);
        userTypeDTO.setDescription("Individual User Type");
        userTypeDTO.setEnabled(true);

        when(userTypeService.addUserType(any(UserTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("The type already exists"));

        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The type already exists"));
    }
}