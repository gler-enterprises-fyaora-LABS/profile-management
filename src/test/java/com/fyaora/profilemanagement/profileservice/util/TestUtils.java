package com.fyaora.profilemanagement.profileservice.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class TestUtils {

    /***************************************************************************************/
    /************************** Waitlist Customer Request Objects **************************/
    /***************************************************************************************/

    public static String getWaitlistCustomerRequestDTO1() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf" }
               """;
    }

    public static String getWaitlistCustomerRequestDTO2() {
        return """
                { "email":"test@hotmail.com", "telnum":null, "postcode":"ox16 9pf" }
               """;
    }

    public static String getWaitlistCustomerRequestDTO3() {
        return """
                { "email":"test@hotmail.com", "telnum":"+44 2255667788", "postcode":null }
               """;
    }

    public static String getWaitlistCustomerRequestDTO4() {
        return """
                { "email":"test@hotmail.com", "telnum":"", "postcode":"" }
               """;
    }

    public static String getWaitlistCustomerRequestDTO5() {
        return """
                { "email":"test@hotmail.com", "telnum":"04422 55667788", "postcode":"ox16 9pf", "extrafield":"test value" }
               """;
    }

    public static String getWaitlistCistomerRequest_negative1() {
        return """
                { "telnum":"+442255667788", "postcode":"ox16 9pf" }
               """;
    }

    public static String getWaitlistCistomerRequest_negative2() {
        return """
                { "email":"invalid-email", "telnum":"+442255667788", "postcode":"ox16 9pf" }
               """;
    }

    public static String getWaitlistCistomerRequest_negative3() {
        return """
                { "email":"", "telnum":"+442255667788", "postcode":"ox16 9pf" }
               """;
    }

    public static String getWaitlistCistomerRequest_negative4() {
        return """
                { "email":null, "telnum":"+442255667788", "postcode":"ox16 9pf" }
               """;
    }

    public static String getWaitlistCistomerRequest_negative5() {
        return """
                { "email":"test@hotmail.com", "telnum":"!!@@##", "postcode":"ox16 9pf" }
               """;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pageNum", "3");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "user1@example.com");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest3() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("telnum", "+447000000004");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest4() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "user1@example.com");
        map.add("telnum", "+447000000001");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest5() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "2025-08-01");
        map.add("to", "2025-08-10");
        map.add("pageNum", "1");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest_negative1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "invalid-email@invalid.com");
        map.add("telnum", "+449999999999");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest_negative2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "invalid-email@invalid.com");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistCustomerRequest_negative3() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("telnum", "+44000000000");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    /***************************************************************************************/
    /************************** Waitlist Investor Request Objects **************************/
    /***************************************************************************************/

    public static String getWaitlistInvestorRequestDTO1() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "name":"Test Company" }
               """;
    }

    public static String getWaitlistInvestorRequestDTO2() {
        return """
                { "email":"test@hotmail.com", "telnum":null, "name":"Test Company" }
               """;
    }

    public static String getWaitlistInvestorRequestDTO3() {
        return """
                { "email":"test@hotmail.com", "telnum":"", "name":"Test Company" }
               """;
    }

    public static String getWaitlistInvestorRequest_negative1() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "name":"" }
               """;
    }

    public static String getWaitlistInvestorRequest_negative2() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "name":null }
               """;
    }

    public static String getWaitlistInvestorRequest_negative3() {
        return """
                { "email":"", "telnum":"+442255667788", "name":"Test Company" }
               """;
    }

    public static String getWaitlistInvestorRequest_negative4() {
        return """
                { "email":null, "telnum":"+442255667788", "name":"Test Company" }
               """;
    }

    public static String getWaitlistInvestorRequest_negative5() {
        return """
                { "email":"invalid-email", "telnum":"+442255667788", "name":"Test Company" }
               """;
    }

    public static String getWaitlistInvestorRequest_negative6() {
        return """
                { "email":"", "telnum":"+442255667788", "name":"" }
               """;
    }

    public static String getWaitlistInvestorRequest_negative7() {
        return """
                { "email":null, "telnum":"+442255667788", "name":null }
               """;
    }

    public static String getWaitlistInvestorRequest_negative8() {
        return """
                { "email":"test@hotmail.com", "telnum":"!!@@##", "name":"Test Company" }
               """;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pageNum", "3");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "2025-08-01");
        map.add("to", "2025-10-10");
        map.add("email", "user81@example.com");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest3() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "2025-08-01");
        map.add("to", "2025-10-10");
        map.add("telnum", "+447000000090");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest4() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "user81@example.com");
        map.add("telnum", "+447000000081");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest_negative1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "2025-08-01");
        map.add("to", "2025-10-10");
        map.add("email", "invalid-email@invalid.com");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest_negative2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "2025-08-01");
        map.add("to", "2025-10-10");
        map.add("telnum", "+449999999999");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest_negative3() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "2025-07-01");
        map.add("to", "2025-07-10");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistInvestorRequest_negative4() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pageNum", "10");
        map.add("pageSize", "10");

        return map;
    }

    /***************************************************************************************/
    /********************** Waitlist Service Provider Request Objects **********************/
    /***************************************************************************************/

    public static String getWaitlistServiceProviderRequestDTO1() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative1() {
        return """
                { "email":"", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative2() {
        return """
                { "email":null, "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative3() {
        return """
                { "email":"invalid-email", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative4() {
        return """
                { "email":"test@hotmail.com", "telnum":"", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative5() {
        return """
                { "email":"test@hotmail.com", "telnum":null, "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative6() {
        return """
                { "email":"test@hotmail.com", "telnum":"!!@@##", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative7() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"", "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative8() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":null, "vendorType":"COMPANY", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative9() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"", "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative10() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":null, "servicesOffered":[1,2] }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative11() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":"" }
               """;
    }


    public static String getWaitlistServiceProviderRequest_negative12() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":null }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative13() {
        return """
                { "email":"", "telnum":"", "postcode": "", "vendorType":null, "servicesOffered":null }
               """;
    }

    public static String getWaitlistServiceProviderRequest_negative14() {
        return """
                { "email":"test@hotmail.com", "telnum":"+442255667788", "postcode":"ox16 9pf", "vendorType":"COMPANY", "servicesOffered":[50,100] }
               """;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pageNum", "3");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "user74@example.com");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest3() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("telnum", "+447000000080");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest4() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "user74@example.com");
        map.add("telnum", "+447000000074");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest5() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pageNum", "3");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchServicesForWaitlistServiceProviderRequest() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "user92@example.com");
        map.add("telnum", "+447000000092");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest_negative1() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "invalid-email@example.com");
        map.add("telnum", "+449999999999");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest_negative2() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "invalid-email@example.com");
        map.add("pageNum", "0");
        map.add("pageSize", "10");

        return map;
    }

    public static MultiValueMap<String, String> searchWaitlistServiceProviderRequest_negative3() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("telnum", "449999999999");
        map.add("pageNum", "0");
        map.add("pageSize", "10");
        map.add("from", "2025-06-01");
        map.add("to", "2025-10-10");

        return map;
    }


    /***************************************************************************************/
    /*********************************** Inquire Objects ***********************************/
    /***************************************************************************************/

    public static String addInquire1() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String addInquire2() {
        return """
                 { "firstName": "", "lastName": "", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String addInquire3() {
        return """
                 { "firstName": "", "lastName": "last name", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String addInquire4() {
        return """
                 { "firstName": null, "lastName": "last name", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String addInquire5() {
        return """
                 { "firstName": "first", "lastName": "", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String addInquire6() {
        return """
                 { "firstName": "first", "lastName": null, "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String addInquire_negative1() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "", "message": "test message" }
                """;
    }

    public static String addInquire_negative2() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": null, "message": "test message" }
                """;
    }

    public static String addInquire_negative3() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "test@hotmail.com", "message": "" }
                """;
    }

    public static String addInquire_negative4() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "test@hotmail.com", "message": null }
                """;
    }

    public static String addInquire_negative5() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "@@#$$", "message": "test message" }
                """;
    }
}
