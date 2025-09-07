package com.fyaora.profilemanagement.profileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import com.fyaora.profilemanagement.profileservice.util.TestUtils;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class InquiryControllerIT {

    private static final String ADD_URL = "/api/v1/inquiry";
    private static final String SEARCH_URL = "/api/v1/inquiry";

    @Autowired
    private InquiryController inquiryController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static Stream<Arguments> provideValidInquiryJson() {
        return Stream.of(
                Arguments.of(TestUtils.addInquire1()),
                Arguments.of(TestUtils.addInquire2()),
                Arguments.of(TestUtils.addInquire3()),
                Arguments.of(TestUtils.addInquire4()),
                Arguments.of(TestUtils.addInquire5()),
                Arguments.of(TestUtils.addInquire6())
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidInquiryJson")
    @DisplayName("Should save inquiry")
    @Sql(scripts = {"classpath:/db/table_clean.sql"})
    void shouldSaveInquireDetails(String json) throws Exception {
        String QUERY = "SELECT first_name, last_name, email, message FROM inquiry WHERE email = ?";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(ADD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully added the inquiry"))
                .andReturn();

        InquiryRequest dto = objectMapper.readValue(json, InquiryRequest.class);
        Inquiry inquiry =
                jdbcTemplate.queryForObject(QUERY, (rs, rowNum) -> Inquiry.builder()
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .email(rs.getString("email"))
                        .message(rs.getString("message"))
                        .build(), dto.email());

        Assertions.assertThat(dto).usingRecursiveComparison().isEqualTo(dto);
    }

    static Stream<Arguments> provideInvalidInquiryJSON() {
        return Stream.of(
                Arguments.of(TestUtils.addInquire_negative1(), "email", "Email must not be empty"),
                Arguments.of(TestUtils.addInquire_negative2(), "email", "Email must not be empty"),
                Arguments.of(TestUtils.addInquire_negative3(), "message", "Message must not be empty"),
                Arguments.of(TestUtils.addInquire_negative4(), "message", "Message must not be empty"),
                Arguments.of(TestUtils.addInquire_negative5(), "email", "Email must be a valid email address")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInquiryJSON")
    @DisplayName("Should not save inquiry")
    @Sql(scripts = {"/db/table_clean.sql"})
    void shouldNotSaveInquireDetails(String json, String field, String msg) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ADD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].message").value(msg));
    }

    @Test
    @DisplayName("Should receive inquiry details")
    @Sql(scripts = "classpath:/db/inquiry_details.sql")
    void shouldRetrieveInquiryDetails() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders
                                .get(SEARCH_URL)
                                .param("from", "2025-08-01")
                                .param("to", "2025-08-30")
                                .param("pageNum", "0")
                                .param("pageSize", "20"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.results.length()").value(20));
    }

    @Test
    @DisplayName("Should support pagination")
    @Sql(scripts = "classpath:/db/inquiry_details.sql")
    void shouldSupportPagination() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SEARCH_URL)
                        .param("from", "2025-08-01")
                        .param("to", "2025-08-30")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.length()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[4].id").value("15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[4].firstName").value("Matthew"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[4].lastName").value("Harris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[4].email").value("matthew.harris@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[4].message").value("I tried to pay but t..."));
    }

    @Test
    @DisplayName("Should retrieved records matched with all search parameters")
    @Sql(scripts = "classpath:/db/inquiry_details.sql")
    void shouldReturnResults_matchedWithAllParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SEARCH_URL)
                        .param("from", "2025-08-01")
                        .param("to", "2025-08-30")
                        .param("email", "matthew.harris@example.com")
                        .param("pageNum", "0")
                        .param("pageSize", "10"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").value("15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].firstName").value("Matthew"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].lastName").value("Harris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].email").value("matthew.harris@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].message").value("I tried to pay but t..."));
    }

    @Test
    @DisplayName("Should retrieved records matched with email")
    @Sql(scripts = "classpath:/db/inquiry_details.sql")
    void shouldReturnResults_matchedWithEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SEARCH_URL)
                        .param("email", "matthew.harris@example.com")
                        .param("pageNum", "0")
                        .param("pageSize", "10"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").value("15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].firstName").value("Matthew"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].lastName").value("Harris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].email").value("matthew.harris@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].message").value("I tried to pay but t..."));
    }

    @Test
    @DisplayName("Should retrieved records matched with default parameters")
    @Sql(scripts = "classpath:/db/inquiry_details.sql")
    void shouldReturnResults_matchedWithDefaultParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SEARCH_URL))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.length()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[9].id").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[9].firstName").value("Sophia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[9].lastName").value("Thomas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[9].email").value("sophia.thomas@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[9].message").value("Your customer suppor..."));
    }

    @Test
    @DisplayName("Should retrieved empty list if criteria is not matched")
    @Sql(scripts = "classpath:/db/inquiry_details.sql")
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(SEARCH_URL)
                        .param("from", "2024-08-01")
                        .param("to", "2024-08-30")
                        .param("email", "matthew.harris@example.com")
                        .param("pageNum", "0")
                        .param("pageSize", "10"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Inquiry requests not found"));
    }
}
