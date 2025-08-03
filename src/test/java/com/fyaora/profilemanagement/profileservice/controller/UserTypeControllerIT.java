package com.fyaora.profilemanagement.profileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeDTO;
import com.fyaora.profilemanagement.profileservice.dto.UserTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = "/scripts/init-user-types.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserTypeControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    private static Stream<Arguments> provideUserTypeParameters() {
        return Stream.of(
                Arguments.of(1, "Service Provider Type Test", true, UserTypeEnum.SERVICE_PROVIDER),
                Arguments.of(2, "Customer Type Test", true, UserTypeEnum.CUSTOMER),
                Arguments.of(3, "Individual Type Test", true, UserTypeEnum.INDIVIDUAL),
                Arguments.of(4, "Business Type Test", true, UserTypeEnum.BUSINESS)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserTypeParameters")
    @DisplayName("Test: Successfully get user type for all UserTypeEnum values via HTTP")
    void testGetUserTypeByType_Success_Http(int id, String description, boolean enabled, UserTypeEnum type) throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value(type.toString()))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.enabled").value(enabled));
    }

    @ParameterizedTest
    @EnumSource(value = UserTypeEnum.class, names = {"SERVICE_PROVIDER", "CUSTOMER", "INDIVIDUAL", "BUSINESS"})
    @DisplayName("Test: Handle UserTypeNotFoundException for disabled user types")
    @Sql(scripts = "/init-user-types.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetUserTypeByType_NotFound_Http(UserTypeEnum type) throws Exception {
        // Arrange: Update the database to disable the user type
        UserTypeDTO updateDTO = new UserTypeDTO();
        updateDTO.setId(0); // ID will be ignored for new entries
        updateDTO.setType(type);
        updateDTO.setDescription("Updated Description");
        updateDTO.setEnabled(false);

        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type/{type}", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS"));
    }

    @Test
    @DisplayName("Test: Handle IllegalArgumentException when user type is null")
    void testGetUserTypeByType_NullInput() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type cannot be null."));
    }

    @Test
    @DisplayName("Test: Validate mandatory fields in UserTypeDTO")
    void testAddUserType_MandatoryFieldsValidation() throws Exception {
        // Arrange: Test case 1 - Null type
        UserTypeDTO nullTypeDTO = new UserTypeDTO();
        nullTypeDTO.setId(5);
        nullTypeDTO.setType(null);
        nullTypeDTO.setDescription("Valid Description");
        nullTypeDTO.setEnabled(true);

        // Act & Assert: Null type
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullTypeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.type").value("User type cannot be null."));

        // Arrange: Test case 2 - Blank description
        UserTypeDTO blankDescriptionDTO = new UserTypeDTO();
        blankDescriptionDTO.setId(5);
        blankDescriptionDTO.setType(UserTypeEnum.INDIVIDUAL);
        blankDescriptionDTO.setDescription("");
        blankDescriptionDTO.setEnabled(true);

        // Act & Assert: Blank description
        mockMvc.perform(post("/api/v1/account-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blankDescriptionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.description").value("Description cannot be blank."));
    }
}