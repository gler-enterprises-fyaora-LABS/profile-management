package com.fyaora.profilemanagement.profileservice.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fyaora.profilemanagement.profileservice.model.request.ServiceOfferedRequest;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistRequest;
import com.fyaora.profilemanagement.profileservice.model.response.deserializer.ServiceOfferedDTODeserializer;
import com.fyaora.profilemanagement.profileservice.model.enums.VendorTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ServiceProviderWaitlist(
        @NotEmpty(message = "{email.notempty}")
        @Email(message = "{email.invalid}")
        @JsonProperty("email")
        String email,

        @JsonProperty("telnum")
        @Pattern(
                regexp = "^\\+?[0-9\\s\\-()]{7,20}$",
                message = "{telnum.invalid.service.provider}"
        )
        String telnum,

        @NotEmpty(message = "{postcode.notempty}")
        @JsonProperty("postcode")
        String postcode,

        @NotNull(message = "{vendor.type.notnull}")
        @JsonProperty("vendorType")
        VendorTypeEnum vendorType,

        @JsonDeserialize(using = ServiceOfferedDTODeserializer.class)
        @NotNull(message = "{service.offered.invalid.format}")
        @JsonProperty(value = "servicesOffered", access = JsonProperty.Access.WRITE_ONLY)
        List<Integer> servicesOffered,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<ServiceOfferedRequest> services

) implements WaitlistRequest {
    public ServiceProviderWaitlist {
        postcode = (null == postcode) ? null : postcode.toUpperCase();
    }
}
