package com.fyaora.profilemanagement.profileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.model.request.InquiryRequest;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import com.fyaora.profilemanagement.profileservice.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
                Arguments.of(TestUtils.getInquire1()),
                Arguments.of(TestUtils.getInquire2()),
                Arguments.of(TestUtils.getInquire3()),
                Arguments.of(TestUtils.getInquire4()),
                Arguments.of(TestUtils.getInquire5()),
                Arguments.of(TestUtils.getInquire6())
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidInquiryJson")
    @DisplayName("Should save inquiry")
    @Sql(scripts = {"/db/table_clean.sql"})
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
                Arguments.of(TestUtils.getInquire_negative1(), "Email must not be empty"),
                Arguments.of(TestUtils.getInquire_negative2(), "Email must not be empty"),
                Arguments.of(TestUtils.getInquire_negative3(), "Message must not be empty"),
                Arguments.of(TestUtils.getInquire_negative4(), "Message must not be empty"),
                Arguments.of(TestUtils.getInquire_negative5(), "Email must be a valid email address")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInquiryJSON")
    @DisplayName("Should not save inquiry")
    @Sql(scripts = {"/db/table_clean.sql"})
    void shouldNotSaveInquireDetails(String json, String msg) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ADD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(msg));
    }
}
