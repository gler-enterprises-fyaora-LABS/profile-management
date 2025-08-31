package com.fyaora.profilemanagement.profileservice.util;

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

    public static String searchWaitlistCustomerRequest1() {
        return """
                 { "email": "", "telnum": "", "page": 3 }
                """;
    }

    public static String searchWaitlistCustomerRequest2() {
        return """
                 { "email": "user1@example.com", "telnum": "", "page": 0 }
                """;
    }

    public static String searchWaitlistCustomerRequest3() {
        return """
                 { "email": "", "telnum": "+447000000004", "page": 0 }
                """;
    }

    public static String searchWaitlistCustomerRequest4() {
        return """
                 { "email": "user1@example.com", "telnum": "+447000000004", "page": 0 }
                """;
    }

    public static String searchWaitlistCustomerRequest_negative1() {
        return """
                 { "email": "invalid-email@invalid.com", "telnum": "+449999999999", "page": 0 }
                """;
    }

    public static String searchWaitlistCustomerRequest_negative2() {
        return """
                 { "email": "invalid-email@invalid.com", "telnum": "", "page": 0 }
                """;
    }

    public static String searchWaitlistCustomerRequest_negative3() {
        return """
                 { "email": "", "telnum": "+449999999999", "page": 0 }
                """;
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

    public static String searchWaitlistInvestorRequest1() {
        return """
                 { "email": "", "telnum": "", "page": 3 }
                """;
    }

    public static String searchWaitlistInvestorRequest2() {
        return """
                 { "email": "user81@example.com", "telnum": "", "page": 0 }
                """;
    }

    public static String searchWaitlistInvestorRequest3() {
        return """
                 { "email": "", "telnum": "+447000000090", "page": 0 }
                """;
    }

    public static String searchWaitlistInvestorRequest4() {
        return """
                 { "email": "user81@example.com", "telnum": "+447000000090", "page": 0 }
                """;
    }

    public static String searchWaitlistInvestorRequest_negative1() {
        return """
                 { "email": "invalid-email@invalid.com", "telnum": "+449999999999", "page": 0 }
                """;
    }

    public static String searchWaitlistInvestorRequest_negative2() {
        return """
                 { "email": "invalid-email@invalid.com", "telnum": "", "page": 0 }
                """;
    }

    public static String searchWaitlistInvestorRequest_negative3() {
        return """
                 { "email": "", "telnum": "+449999999999", "page": 0 }
                """;
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

    public static String searchWaitlistServiceProviderRequest1() {
        return """
                 { "email": "", "telnum": "", "page": 3 }
                """;
    }

    public static String searchWaitlistServiceProviderRequest2() {
        return """
                 { "email": "user74@example.com", "telnum": "", "page": 0 }
                """;
    }

    public static String searchWaitlistServiceProviderRequest3() {
        return """
                 { "email": "", "telnum": "+447000000080", "page": 0 }
                """;
    }

    public static String searchWaitlistServiceProviderRequest4() {
        return """
                 { "email": "user74@example.com", "telnum": "+447000000080", "page": 0 }
                """;
    }

    public static String searchServicesForWaitlistServiceProviderRequest() {
        return """
                 { "email": "user92@example.com", "telnum": "+447000000092", "page": 0 }
                """;
    }

    public static String searchWaitlistServiceProviderRequest_negative1() {
        return """
                 { "email": "invalid-email@invalid.com", "telnum": "+449999999999", "page": 0 }
                """;
    }

    public static String searchWaitlistServiceProviderRequest_negative2() {
        return """
                 { "email": "invalid-email@invalid.com", "telnum": "", "page": 0 }
                """;
    }

    public static String searchWaitlistServiceProviderRequest_negative3() {
        return """
                 { "email": "", "telnum": "+449999999999", "page": 0 }
                """;
    }


    /***************************************************************************************/
    /*********************************** Inquire Objects ***********************************/
    /***************************************************************************************/

    public static String getInquire1() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String getInquire2() {
        return """
                 { "firstName": "", "lastName": "", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String getInquire3() {
        return """
                 { "firstName": "", "lastName": "last name", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String getInquire4() {
        return """
                 { "firstName": null, "lastName": "last name", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String getInquire5() {
        return """
                 { "firstName": "first", "lastName": "", "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String getInquire6() {
        return """
                 { "firstName": "first", "lastName": null, "email": "test@hotmail.com", "message": "test message" }
                """;
    }

    public static String getInquire_negative1() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "", "message": "test message" }
                """;
    }

    public static String getInquire_negative2() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": null, "message": "test message" }
                """;
    }

    public static String getInquire_negative3() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "test@hotmail.com", "message": "" }
                """;
    }

    public static String getInquire_negative4() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "test@hotmail.com", "message": null }
                """;
    }

    public static String getInquire_negative5() {
        return """
                 { "firstName": "first", "lastName": "last name", "email": "@@#$$", "message": "test message" }
                """;
    }
}
