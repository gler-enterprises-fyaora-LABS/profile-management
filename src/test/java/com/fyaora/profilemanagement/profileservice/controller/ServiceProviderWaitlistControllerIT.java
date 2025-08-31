package com.fyaora.profilemanagement.profileservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyaora.profilemanagement.profileservice.dto.WaitlistServiceProviderRequestDTO;
import com.fyaora.profilemanagement.profileservice.model.db.entity.VendorTypeEnum;
import com.fyaora.profilemanagement.profileservice.util.TestUtils;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

            WaitlistServiceProviderRequestDTO deObj = objectMapper.readValue(dto, WaitlistServiceProviderRequestDTO.class);
            WaitlistServiceProviderRequestDTO actualFromDb = getWaitlistRequestFromDB(deObj.email());

            Assertions.assertThat(deObj.email()).isEqualTo(actualFromDb.email());
            Assertions.assertThat(deObj.telnum()).isEqualTo(actualFromDb.telnum());
            Assertions.assertThat(deObj.postcode()).isEqualTo(actualFromDb.postcode());

            List<Integer> serviceIds = getServiceIds(deObj.email());
            Assertions.assertThat(serviceIds).containsExactlyInAnyOrderElementsOf(deObj.servicesOffered());
        }
    }

    @Nested
    class NegativeScenarios {
        static Stream<Arguments> provideWaitlistServiceProviderRequest_nagative() {
            String requestDTO1 = TestUtils.getWaitlistServiceProviderRequest_negative1();
            String requestDTO2 = TestUtils.getWaitlistServiceProviderRequest_negative2();
            String requestDTO3 = TestUtils.getWaitlistServiceProviderRequest_negative3();
            String requestDTO4 = TestUtils.getWaitlistServiceProviderRequest_negative4();
            String requestDTO5 = TestUtils.getWaitlistServiceProviderRequest_negative5();
            String requestDTO6 = TestUtils.getWaitlistServiceProviderRequest_negative6();
            String requestDTO7 = TestUtils.getWaitlistServiceProviderRequest_negative7();
            String requestDTO8 = TestUtils.getWaitlistServiceProviderRequest_negative8();
            String requestDTO9 = TestUtils.getWaitlistServiceProviderRequest_negative9();
            String requestDT10 = TestUtils.getWaitlistServiceProviderRequest_negative10();
            String requestDT11 = TestUtils.getWaitlistServiceProviderRequest_negative11();
            String requestDT12 = TestUtils.getWaitlistServiceProviderRequest_negative12();
            String requestDT13 = TestUtils.getWaitlistServiceProviderRequest_negative13();
            String requestDT14 = TestUtils.getWaitlistServiceProviderRequest_negative14();

            return Stream.of(
                    Arguments.of(requestDTO1, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO2, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must not be empty")),
                    Arguments.of(requestDTO3, MockMvcResultMatchers.status().isBadRequest(), List.of("Email must be a valid email address")),
                    Arguments.of(requestDTO4, MockMvcResultMatchers.status().isBadRequest(), List.of("Invalid telephone number format or telephone umber is empty")),
                    Arguments.of(requestDTO5, MockMvcResultMatchers.status().isBadRequest(), List.of("Phone Number must not be empty")),
                    Arguments.of(requestDTO6, MockMvcResultMatchers.status().isBadRequest(), List.of("Invalid telephone number format or telephone umber is empty")),
                    Arguments.of(requestDTO7, MockMvcResultMatchers.status().isBadRequest(), List.of("Postcode must not be empty")),
                    Arguments.of(requestDTO8, MockMvcResultMatchers.status().isBadRequest(), List.of("Postcode must not be empty")),
                    Arguments.of(requestDTO9, MockMvcResultMatchers.status().isBadRequest(), List.of("Vendor Type must be either INDEPENDENT or COMPANY")),
                    Arguments.of(requestDT10, MockMvcResultMatchers.status().isBadRequest(), List.of("Vendor Type must be either INDEPENDENT or COMPANY")),
                    Arguments.of(requestDT11, MockMvcResultMatchers.status().isBadRequest(), List.of("servicesOffered must be an array. Format should be [1,2,3]")),
                    Arguments.of(requestDT12, MockMvcResultMatchers.status().isBadRequest(), List.of("servicesOffered must be an array. Format should be [1,2,3]")),
                    Arguments.of(requestDT13, MockMvcResultMatchers.status().isBadRequest(),
                            List.of("Email must not be empty", "Invalid telephone number format or telephone umber is empty",
                                    "Postcode must not be empty", "Vendor Type must be either INDEPENDENT or COMPANY", "servicesOffered must be an array. Format should be [1,2,3]")),
                    Arguments.of(requestDT14, MockMvcResultMatchers.status().isNotFound(), List.of("The following service IDs were not found: [50, 100]"))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWaitlistServiceProviderRequest_nagative")
        @DisplayName("Should not save service provider waitlist request")
        @Sql(scripts = {"/db/table_clean.sql", "/db/add_services.sql"})
        void shouldNotSaveServiceProviderWaitlistRequest(String dto, ResultMatcher status, List<String> messages) throws Exception {
            MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders.post(JOIN_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dto))
                    .andExpect(status)
                    .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            String resMessages = JsonPath.read(responseJson, "$.message");
            List<String> mList = Arrays.stream(resMessages.split(";")).map(String::trim).collect(Collectors.toList());

            Assertions.assertThat(messages).containsExactlyInAnyOrderElementsOf(mList);

            String SQL = "SELECT COUNT(*) FROM waitlist";
            long count = jdbcTemplate.queryForObject(SQL, Long.class);
            Assertions.assertThat(count).isEqualTo(0);
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
                    Arguments.of(TestUtils.searchWaitlistServiceProviderRequest4(), 2)
            );
        }

        @ParameterizedTest
        @MethodSource("provideSearchDTOForPositiveScenarios")
        @DisplayName("Should retrieve service provider waitlist request")
        void shouldRetrieveServiceProviderWaitlistRecords(String searchDto, int size) throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders
                                            .post(SEARCH_URL)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(searchDto))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();
            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            org.junit.jupiter.api.Assertions.assertNotNull(results);
            org.junit.jupiter.api.Assertions.assertTrue(results.isArray());
            org.junit.jupiter.api.Assertions.assertEquals(size, results.size());
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
        void shouldNotRetrieveServiceProviderWaitlistRecords(String searchDto) throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post(SEARCH_URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(searchDto))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Service provider waitlist requests not found"));
        }

        @Test
        @DisplayName("Should return services for given service provider waitlist")
        void shouldReturnServicesForServiceProvider() throws Exception {
            MvcResult result =
                    mockMvc.perform(
                                    MockMvcRequestBuilders.post(SEARCH_URL)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(TestUtils.searchServicesForWaitlistServiceProviderRequest()))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andReturn();

            String responseJson = result.getResponse().getContentAsString();
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode results = root.get("results");

            Assertions.assertThat(results).isNotNull();
            Assertions.assertThat(results.isArray()).isTrue();

            JsonNode services = root.get("results").get(0).get("services");
            Assertions.assertThat(services).isNotNull();
            Assertions.assertThat(services.isArray()).isTrue();
            Assertions.assertThat(services.size()).isEqualTo(3);

            List<String> serviceList = new ArrayList<>();
            services.forEach(s -> serviceList.add(s.get("name").asText()));
            List<String> expected = List.of("Carpentry", "Cleaning", "Pest Control");
            Assertions.assertThat(serviceList).containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    private WaitlistServiceProviderRequestDTO getWaitlistRequestFromDB(String email) {
        String sql = "SELECT email, telnum, postcode, vendor_type FROM waitlist WHERE email = ?";
        WaitlistServiceProviderRequestDTO actualFromDb =
                jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        WaitlistServiceProviderRequestDTO.builder()
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
