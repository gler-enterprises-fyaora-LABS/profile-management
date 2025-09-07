package com.fyaora.profilemanagement.profileservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.model.response.CustomerWaitlist;
import com.fyaora.profilemanagement.profileservice.util.TestUtils;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
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
class CustomerWaitlistControllerIT {

    private static final String JOIN_URL = "/api/v1/waitlist/customer/join";
    private static final String SEARCH_URL = "/api/v1/waitlist/customer/search";

    @Autowired
    private CustomerWaitlistController customerWaitlistController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class PositiveJoinScenarios {

        static Stream<Arguments> provideWaitlistCustomerRequest() {
            String requestDTO1 = TestUtils.getWaitlistCustomerRequestDTO1();
            String requestDTO2 = TestUtils.getWaitlistCustomerRequestDTO2();
            String requestDTO3 = TestUtils.getWaitlistCustomerRequestDTO3();
            String requestDTO4 = TestUtils.getWaitlistCustomerRequestDTO4();
            String requestDTO5 = TestUtils.getWaitlistCustomerRequestDTO5();

            return Stream.of(
                    Arguments.of(requestDTO1),
                    Arguments.of(requestDTO2),
                    Arguments.of(requestDTO3),
                    Arguments.of(requestDTO4),
                    Arguments.of(requestDTO5)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistCustomerRequest")
        @DisplayName("Should save customer waitlist request")
        @Sql(scripts = "/db/table_clean.sql")
        void shouldSaveCustomerWaitlistRequest(String dto) throws Exception {
            mockMvc.perform(
                    MockMvcRequestBuilders.post(JOIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully joined the waitlist"));

            CustomerWaitlist deObj = objectMapper.readValue(dto, CustomerWaitlist.class);
            CustomerWaitlist actualFromDb = getWaitlistRequestFromDB(deObj.email());

            Assertions.assertEquals(deObj.email(), actualFromDb.email(), "Email should match");
            Assertions.assertEquals(deObj.telnum(), actualFromDb.telnum(), "Telnum should match");
            Assertions.assertEquals(deObj.postcode(), actualFromDb.postcode(), "Postcode should match");
        }
    }

    @Nested
    class NegativeJoinScenarios {

        static Stream<Arguments> provideWaitlistCustomerRequest_nagative() {
            String requestDTO1 = TestUtils.getWaitlistCistomerRequest_negative1();
            String requestDTO2 = TestUtils.getWaitlistCistomerRequest_negative2();
            String requestDTO3 = TestUtils.getWaitlistCistomerRequest_negative3();
            String requestDTO4 = TestUtils.getWaitlistCistomerRequest_negative4();
            String requestDTO5 = TestUtils.getWaitlistCistomerRequest_negative5();

            return Stream.of(
                    Arguments.of(requestDTO1, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO2, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must be a valid email address")),
                    Arguments.of(requestDTO3, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO4, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO5, MockMvcResultMatchers.status().isBadRequest(), List.of("Invalid telephone number format"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistCustomerRequest_nagative")
        @DisplayName("Should not save customer waitlist request")
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

            org.assertj.core.api.Assertions.assertThat(actualMessages)
                    .containsExactlyInAnyOrderElementsOf(messages);

            String SQL = "SELECT COUNT(*) FROM waitlist";
            long count = jdbcTemplate.queryForObject(SQL, Long.class);
            Assertions.assertEquals(count, 0);
        }
    }

    @Nested
    @Sql(scripts = {"/db/table_clean.sql", "/db/waitlist_for_searching.sql"})
    class SearchScenarios {

        static Stream<Arguments> provideSearchDTOForPositiveScenarios() {
            return Stream.of(
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest1(), 4),
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest2(), 1),
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest3(), 1),
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest4(), 1),
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest5(), 3)
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForPositiveScenarios")
        @DisplayName("Should retrieve customer waitlist request")
        void shouldRetrieveCustomerWaitlistRecords(MultiValueMap<String, String> params, int size) throws Exception {
                MvcResult result =
                        mockMvc.perform(
                                MockMvcRequestBuilders.get(SEARCH_URL)
                                        .params(params))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            Assertions.assertNotNull(results);
            Assertions.assertTrue(results.isArray());
            Assertions.assertEquals(size, results.size());
        }

        static Stream<Arguments> provideSearchDTOForNegativeScenario() {
            return Stream.of(
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest_negative1()),
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest_negative2()),
                    Arguments.of(TestUtils.searchWaitlistCustomerRequest_negative3())
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForNegativeScenario")
        @DisplayName("Should not retrieve customer waitlist request")
        void shouldNotRetrieveCustomerWaitlistRecords(MultiValueMap<String, String> params) throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get(SEARCH_URL)
                                    .params(params)
                    )
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer waitlist requests not found"));
        }

        @Test
        @DisplayName("Should retrieve customer waitlist request for default vales")
        void shouldRetrieveCustomerWaitlistRecords_forDefaultParamValues() throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders.get(SEARCH_URL))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            Assertions.assertNotNull(results);
            Assertions.assertTrue(results.isArray());
            Assertions.assertEquals(10, results.size());
        }
    }

    private CustomerWaitlist getWaitlistRequestFromDB(String email) {
        String sql = "SELECT email, telnum, postcode FROM waitlist WHERE email = ?";
        CustomerWaitlist actualFromDb =
                jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        CustomerWaitlist.builder()
                                .email(rs.getString("email"))
                                .telnum(rs.getString("telnum"))
                                .postcode(rs.getString("postcode"))
                                .build(), email);
        return actualFromDb;
    }
}
