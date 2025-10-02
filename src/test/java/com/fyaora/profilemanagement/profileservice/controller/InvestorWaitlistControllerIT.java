package com.fyaora.profilemanagement.profileservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.model.request.InvestorWaitlist;
import com.fyaora.profilemanagement.profileservice.util.TestUtils;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class InvestorWaitlistControllerIT {

    private static final String JOIN_URL = "/api/v1/waitlist/investor/join";
    private static final String SEARCH_URL = "/api/v1/waitlist/investor/search";

    @Autowired
    private InvestorWaitlistController investorWaitlistController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class PositiveScenarios {

        static Stream<Arguments> provideWaitlistInvestorRequest() {
            String requestDTO1 = TestUtils.getWaitlistInvestorRequestDTO1();
            String requestDTO2 = TestUtils.getWaitlistInvestorRequestDTO2();
            String requestDTO3 = TestUtils.getWaitlistInvestorRequestDTO3();

            return Stream.of(
                    Arguments.of(requestDTO1),
                    Arguments.of(requestDTO2),
                    Arguments.of(requestDTO3)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistInvestorRequest")
        @DisplayName("Should save investor waitlist request")
        @Sql(scripts = "/db/table_clean.sql")
        void shouldSaveInvestorWaitlistRequest(String dto) throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully joined the waitlist"));

            InvestorWaitlist deObj = objectMapper.readValue(dto, InvestorWaitlist.class);
            InvestorWaitlist actualFromDb = getWaitlistRequestFromDB(deObj.email());

            assertThat(deObj.email()).isEqualTo(actualFromDb.email());
            assertThat(deObj.telnum()).isEqualTo(actualFromDb.telnum());
            assertThat(deObj.name()).isEqualTo(actualFromDb.name());
        }
    }

    @Nested
    class NegativeScenarios {

        @Test
        @DisplayName("Should not save duplicated waitlist request")
        @Sql(scripts = "/db/table_clean.sql")
        void shouldNotSaveDuplicatedInvestorWaitlistRequest() throws Exception {
            String dto = TestUtils.getWaitlistInvestorRequestDTO1();

            mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully joined the waitlist"));

            InvestorWaitlist deObj = objectMapper.readValue(dto, InvestorWaitlist.class);
            InvestorWaitlist actualFromDb = getWaitlistRequestFromDB(deObj.email());

            assertThat(deObj.email()).isEqualTo(actualFromDb.email());
            assertThat(deObj.telnum()).isEqualTo(actualFromDb.telnum());
            assertThat(deObj.name()).isEqualTo(actualFromDb.name());

            // Duplicate waitlist request
            MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            String actualMessages = JsonPath.parse(responseJson).read("$.message");

            assertThat(actualMessages).contains("Waitlist request already exists for email: test@hotmail.com");

            String SQL = "SELECT COUNT(*) FROM waitlist";
            long count = jdbcTemplate.queryForObject(SQL, Long.class);
            assertEquals(1, count);
        }

        static Stream<Arguments> provideWaitlistInvestorRequest_nagative() {
            String requestDTO1 = TestUtils.getWaitlistInvestorRequest_negative1();
            String requestDTO2 = TestUtils.getWaitlistInvestorRequest_negative2();
            String requestDTO3 = TestUtils.getWaitlistInvestorRequest_negative3();
            String requestDTO4 = TestUtils.getWaitlistInvestorRequest_negative4();
            String requestDTO5 = TestUtils.getWaitlistInvestorRequest_negative5();
            String requestDTO6 = TestUtils.getWaitlistInvestorRequest_negative6();
            String requestDTO7 = TestUtils.getWaitlistInvestorRequest_negative7();
            String requestDTO8 = TestUtils.getWaitlistInvestorRequest_negative8();

            return Stream.of(
                    Arguments.of(requestDTO1, MockMvcResultMatchers.status().isBadRequest(), List.of("Company Name/Full Name must not be empty")),
                    Arguments.of(requestDTO2, MockMvcResultMatchers.status().isBadRequest(), List.of("Company Name/Full Name must not be empty")),
                    Arguments.of(requestDTO3, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO4, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO5, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must be a valid email address")),
                    Arguments.of(requestDTO6, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty", "Company Name/Full Name must not be empty")),
                    Arguments.of(requestDTO7, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty", "Company Name/Full Name must not be empty")),
                    Arguments.of(requestDTO8, MockMvcResultMatchers.status().isBadRequest(), List.of("Invalid telephone number format"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistInvestorRequest_nagative")
        @DisplayName("Should not save investor waitlist request")
        @Sql(scripts = "/db/table_clean.sql")
        void shouldNotSaveCustomerWaitlistRequest(String dto, ResultMatcher status, List<String> messages) throws Exception {
            MvcResult result = mockMvc.perform(
                                        MockMvcRequestBuilders.post(JOIN_URL)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(dto))
                                .andExpect(status)
                                .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            List<String> actualMessages = JsonPath.parse(responseJson).read("$.fieldErrors[*].message");

            assertThat(actualMessages)
                    .containsExactlyInAnyOrderElementsOf(messages);

            String SQL = "SELECT COUNT(*) FROM waitlist";
            long count = jdbcTemplate.queryForObject(SQL, Long.class);
            assertThat(count).isEqualTo(0);
        }
    }

    @Nested
    @Sql(scripts = {"/db/table_clean.sql", "/db/waitlist_for_searching.sql"})
    class SearchScenarios {

        static Stream<Arguments> provideSearchDTOForPositiveScenarios() {
            return Stream.of(
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest1(), 3),
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest2(), 1),
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest3(), 1),
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest4(), 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForPositiveScenarios")
        @DisplayName("Should retrieve Investor waitlist request")
        void shouldRetrieveInvestorWaitlistRecords(MultiValueMap<String, String> params, int size) throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders.get(SEARCH_URL)
                                            .params(params))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            assertNotNull(results);
            assertTrue(results.isArray());
            assertEquals(size, results.size());
        }

        @Test
        @DisplayName("Should retrieve Investor waitlist request for default parameter values")
        void shouldRetrieveInvestorWaitlistRecords_forDefaultParameters() throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders.get(SEARCH_URL))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            assertThat(results).isNotNull().isNotEmpty();
            assertThat(results.isArray()).isTrue();
            assertThat(results.size()).isEqualTo(10);
            assertThat(results.get(9).get("email").textValue()).isEqualTo("user30@example.com");
            assertThat(results.get(9).get("telnum").textValue()).isEqualTo("+447000000030");
            assertThat(results.get(9).get("name").textValue()).isEqualTo("User30");
        }

        static Stream<Arguments> provideSearchDTOForNegativeScenario() {
            return Stream.of(
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest_negative1()),
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest_negative2()),
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest_negative3()),
                    Arguments.of(TestUtils.searchWaitlistInvestorRequest_negative4())
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForNegativeScenario")
        @DisplayName("Should not retrieve Investor waitlist request")
        void shouldNotRetrieveInvestorWaitlistRecords(MultiValueMap<String, String> param) throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get(SEARCH_URL)
                                    .params(param))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Investor waitlist requests not found"));
        }
    }


    private InvestorWaitlist getWaitlistRequestFromDB(String email) {
        String sql = "SELECT email, telnum, name FROM waitlist WHERE email = ?";
        InvestorWaitlist actualFromDb =
                jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        InvestorWaitlist.builder()
                                .email(rs.getString("email"))
                                .telnum(rs.getString("telnum"))
                                .name(rs.getString("name"))
                                .build(), email);
        return actualFromDb;
    }
}
