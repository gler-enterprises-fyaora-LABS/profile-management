package com.fyaora.profilemanagement.profileservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import com.fyaora.profilemanagement.profileservice.model.db.repository.ServicesOfferedRepository;
import com.fyaora.profilemanagement.profileservice.model.request.ServiceOfferedRequest;
import com.fyaora.profilemanagement.profileservice.util.TestUtils;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class ServiceOfferedControllerIT {

    private static final String ADD_SERVICE_URL = "/api/v1/waitlist/services";
    private static final String SEARCH_SERVICE_URL = "/api/v1/waitlist/services";

    @Autowired
    private ServiceOfferedController serviceOfferedController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServicesOfferedRepository servicesOfferedRepository;

    @Test
    @DisplayName("Should save service offered")
    @Sql(scripts = "/db/table_clean.sql")
    void shouldSaveServiceOffered() throws Exception {
        String serviceOffered = TestUtils.addService_offered();
        mockMvc.perform(
                        MockMvcRequestBuilders.post(ADD_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(serviceOffered))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully added the service"));

        List<ServiceOffered> services = servicesOfferedRepository.findAll();

        assertEquals(1, services.size());
        assertEquals("service01", services.stream().findFirst().get().getName());
        assertEquals("Test service", services.stream().findFirst().get().getDescription());
    }

    static Stream<Arguments> provideServiceOfferedData() {
        return Stream.of(
                Arguments.of(TestUtils.addServiceOffered_negative1()),
                Arguments.of(TestUtils.addServiceOffered_negative2())
        );
    }

    @ParameterizedTest
    @MethodSource("provideServiceOfferedData")
    @DisplayName("Should validate service offered request")
    @Sql(scripts = "/db/table_clean.sql")
    void shouldSaveServiceOfferedRequest(String body) throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post(ADD_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        List<String> actualMessages = JsonPath.parse(responseJson).read("$.fieldErrors[*].message");

        assertThat(actualMessages)
                .containsExactlyInAnyOrderElementsOf(List.of("Service name must not be empty"));

        String SQL = "SELECT COUNT(*) FROM service_offered";
        long count = jdbcTemplate.queryForObject(SQL, Long.class);
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Should return services from the database")
    @Sql(scripts = {"/db/table_clean.sql", "/db/add_services.sql"})
    void shouldReturnServiceOffered() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(SEARCH_SERVICE_URL)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ServiceOfferedRequest> list = objectMapper.readValue(jsonResponse, new TypeReference<List<ServiceOfferedRequest>>() {});

        assertEquals(3, list.size());
    }
}
