package com.fyaora.profilemanagement.profileservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.model.response.ServiceProviderWaitlist;
import com.fyaora.profilemanagement.profileservice.model.enums.VendorTypeEnum;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class ServiceProviderWaitlistControllerIT {

    private static final String JOIN_URL = "/api/v1/waitlist/service-provider/join";
    private static final String SEARCH_URL = "/api/v1/waitlist/service-provider/search";

    @Autowired
    private ServiceProviderWaitlistController serviceProviderWaitlistController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class PositiveScenarios {
        static Stream<Arguments> provideWaitlistServiceProviderRequest() {
            String requestDTO1 = TestUtils.getWaitlistServiceProviderRequestDTO1();

            return Stream.of(
                    Arguments.of(requestDTO1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistServiceProviderRequest")
        @DisplayName("Should save service provider waitlist requests")
        @Sql(scripts = {"/db/table_clean.sql", "/db/add_services.sql"})
        void shouldSaveServiceProviderWaitlistRequest(String dto) throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully joined the waitlist"));

            ServiceProviderWaitlist deObj = objectMapper.readValue(dto, ServiceProviderWaitlist.class);
            ServiceProviderWaitlist actualFromDb = getWaitlistRequestFromDB(deObj.email());

            assertThat(deObj.email()).isEqualTo(actualFromDb.email());
            assertThat(deObj.telnum()).isEqualTo(actualFromDb.telnum());
            assertThat(deObj.postcode()).isEqualTo(actualFromDb.postcode());

            List<Integer> serviceIds = getServiceIds(deObj.email());
            assertThat(serviceIds).containsExactlyInAnyOrderElementsOf(deObj.servicesOffered());
        }
    }

    @Nested
    class NegativeScenarios {
        static Stream<Arguments> provideWaitlistServiceProviderRequest_nagativeWithFieldErrors() {
            String requestDTO1 = TestUtils.getWaitlistServiceProviderRequest_negative1();
            String requestDTO2 = TestUtils.getWaitlistServiceProviderRequest_negative2();
            String requestDTO3 = TestUtils.getWaitlistServiceProviderRequest_negative3();
            String requestDTO4 = TestUtils.getWaitlistServiceProviderRequest_negative4();
            String requestDTO6 = TestUtils.getWaitlistServiceProviderRequest_negative6();
            String requestDTO7 = TestUtils.getWaitlistServiceProviderRequest_negative7();
            String requestDTO8 = TestUtils.getWaitlistServiceProviderRequest_negative8();
            String requestDT10 = TestUtils.getWaitlistServiceProviderRequest_negative10();
            String requestDT12 = TestUtils.getWaitlistServiceProviderRequest_negative12();
            String requestDT13 = TestUtils.getWaitlistServiceProviderRequest_negative13();

            return Stream.of(
                    Arguments.of(requestDTO1, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO2, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO3, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must be a valid email address")),
                    Arguments.of(requestDTO4, MockMvcResultMatchers.status().isBadRequest(), List.of("Invalid telephone number format or telephone umber is empty")),
                    Arguments.of(requestDTO6, MockMvcResultMatchers.status().isBadRequest(), List.of("Invalid telephone number format or telephone umber is empty")),
                    Arguments.of(requestDTO7, MockMvcResultMatchers.status().isBadRequest(), List.of("Postcode must not be empty")),
                    Arguments.of(requestDTO8, MockMvcResultMatchers.status().isBadRequest(), List.of("Postcode must not be empty")),
                    Arguments.of(requestDT10, MockMvcResultMatchers.status().isBadRequest(), List.of("Vendor Type must be either INDEPENDENT or COMPANY")),
                    Arguments.of(requestDT12, MockMvcResultMatchers.status().isBadRequest(), List.of("servicesOffered must be an array. Format should be [1,2,3]")),
                    Arguments.of(requestDT13, MockMvcResultMatchers.status().isBadRequest(),
                            List.of("Email must not be empty", "Invalid telephone number format or telephone umber is empty",
                                    "Postcode must not be empty", "Vendor Type must be either INDEPENDENT or COMPANY", "servicesOffered must be an array. Format should be [1,2,3]"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistServiceProviderRequest_nagativeWithFieldErrors")
        @DisplayName("Should not save service provider waitlist request")
        @Sql(scripts = {"/db/table_clean.sql", "/db/add_services.sql"})
        void shouldNotSaveServiceProviderWaitlistRequest_withFieldErrors(
                String dto,
                ResultMatcher status,
                List<String> expectedMessages
        ) throws Exception {
            MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(status)
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();

            List<String> actualMessages;
            actualMessages = JsonPath.parse(responseJson).read("$.fieldErrors[*].message");

            assertThat(actualMessages)
                    .containsExactlyInAnyOrderElementsOf(expectedMessages);

            String SQL = "SELECT COUNT(*) FROM waitlist";
            long count = jdbcTemplate.queryForObject(SQL, Long.class);
            assertThat(count).isEqualTo(0);
        }

        static Stream<Arguments> provideWaitlistServiceProviderRequest_nagativeWithMessage() {
            String requestDTO5 = TestUtils.getWaitlistServiceProviderRequest_negative5();
            String requestDTO9 = TestUtils.getWaitlistServiceProviderRequest_negative9();
            String requestDT11 = TestUtils.getWaitlistServiceProviderRequest_negative11();
            String requestDT14 = TestUtils.getWaitlistServiceProviderRequest_negative14();

            return Stream.of(
                    Arguments.of(requestDTO5, MockMvcResultMatchers.status().isBadRequest(), List.of("Phone Number must not be empty")),
                    Arguments.of(requestDTO9, MockMvcResultMatchers.status().isBadRequest(), List.of("Vendor Type must be either INDEPENDENT or COMPANY")),
                    Arguments.of(requestDT11, MockMvcResultMatchers.status().isBadRequest(), List.of("servicesOffered must be an array. Format should be [1,2,3]")),
                    Arguments.of(requestDT14, MockMvcResultMatchers.status().isNotFound(), List.of("The following service IDs were not found: [50, 100]"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistServiceProviderRequest_nagativeWithMessage")
        @DisplayName("Should not save service provider waitlist request")
        @Sql(scripts = {"/db/table_clean.sql", "/db/add_services.sql"})
        void shouldNotSaveServiceProviderWaitlistRequest_withoutMessage(
                String dto,
                ResultMatcher status,
                List<String> expectedMessages
        ) throws Exception {
            MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(status)
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();

            List<String> actualMessages;
            String message = JsonPath.parse(responseJson).read("$.message", String.class);
            actualMessages = List.of(message);

            assertThat(actualMessages)
                    .containsExactlyInAnyOrderElementsOf(expectedMessages);

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
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest1(), 3),
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest2(), 1),
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest3(), 1),
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest4(), 1),
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest5(), 3)
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForPositiveScenarios")
        @DisplayName("Should retrieve service provider waitlist request")
        void shouldRetrieveServiceProviderWaitlistRecords(MultiValueMap<String, String> mapParams, int size) throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders.get(SEARCH_URL).params(mapParams))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            org.junit.jupiter.api.Assertions.assertNotNull(results);
            org.junit.jupiter.api.Assertions.assertTrue(results.isArray());
            org.junit.jupiter.api.Assertions.assertEquals(size, results.size());
        }

        @Test
        @DisplayName("Should retrieve service provider waitlist request for default parameter values")
        void shouldRetrieveServiceProviderWaitlistRecords_withDefaultParameters() throws Exception {
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
            assertThat(results.get(1).get("email").textValue()).isEqualTo("user5@example.com");
            assertThat(results.get(1).get("telnum").textValue()).isEqualTo("+447000000005");
            assertThat(results.get(1).get("postcode").textValue()).isEqualTo("B1 1AA");
            assertThat(results.get(1).get("vendorType").textValue()).isEqualTo("INDEPENDENT");

            JsonNode user5 = results.get(1);
            JsonNode servicesNode = user5.get("services");

            // Convert to List<Map<String, Object>>
            List<Map<String, Object>> actualServices = objectMapper.convertValue(
                    servicesNode, new TypeReference<>() {}
            );

            // Expected list
            List<Map<String, Object>> expectedServices = List.of(
                    Map.of(
                            "id", 2,
                            "name", "Electrical",
                            "description", "Residential and commercial electrical installations, rewiring, and fault fixing"
                    ),
                    Map.of(
                            "id", 3,
                            "name", "Landscaping",
                            "description", "Garden design, lawn care, and outdoor maintenance"
                    ),
                    Map.of(
                            "id", 8,
                            "name", "HVAC",
                            "description", "Heating, ventilation, and air conditioning installation and maintenance"
                    )
            );

            // Assert without order sensitivity
            assertThat(actualServices)
                    .usingRecursiveFieldByFieldElementComparatorIgnoringFields() // deep compare
                    .containsExactlyInAnyOrderElementsOf(expectedServices);


        }

        static Stream<Arguments> provideSearchDTOForNegativeScenario() {
            return Stream.of(
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest_negative1()),
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest_negative2()),
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest_negative3())
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForNegativeScenario")
        @DisplayName("Should not retrieve Service Provider waitlist request")
        void shouldNotRetrieveServiceProviderWaitlistRecords(MultiValueMap<String, String> mapParams) throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get(SEARCH_URL).params(mapParams))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Service provider waitlist requests not found"));
        }

        @Test
        @DisplayName("Should return services for given service provider waitlist")
        void shouldReturnServicesForServiceProvider() throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders.get(SEARCH_URL)
                                            .params(TestUtils.searchServicesForWaitlistServiceProviderRequest()))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            assertThat(results).isNotNull();
            assertThat(results.isArray()).isTrue();

            JsonNode services = root.get("results").get(0).get("services");
            assertThat(services).isNotNull();
            assertThat(services.isArray()).isTrue();
            assertThat(services.size()).isEqualTo(3);

            List<String> serviceList = new ArrayList<>();
            services.forEach(s -> serviceList.add(s.get("name").asText()));
            List<String> expected = List.of("Carpentry", "Cleaning", "Pest Control");
            assertThat(serviceList).containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    private ServiceProviderWaitlist getWaitlistRequestFromDB(String email) {
        String sql = "SELECT email, telnum, postcode, vendor_type FROM waitlist WHERE email = ?";
        ServiceProviderWaitlist actualFromDb =
                jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        ServiceProviderWaitlist.builder()
                                .email(rs.getString("email"))
                                .telnum(rs.getString("telnum"))
                                .postcode(rs.getString("postcode"))
                                .vendorType(VendorTypeEnum.fromCode(rs.getInt("vendor_type")))
                                .build(), email);
        return actualFromDb;
    }

    private List<Integer> getServiceIds(String email) {
        String sql = "SELECT ws.service_id AS service_id FROM GlerProfile.waitlist_service ws " +
                "RIGHT JOIN waitlist w ON w.id = ws.waitlist_id AND w.email = ?";
        List<Integer> serviceIds = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("service_id"), email);
        return serviceIds;
    }
}
