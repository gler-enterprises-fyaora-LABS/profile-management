package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fyaora.profilemanagement.profileservice.model.db.entity.VendorTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record WaitlistServiceProviderRequestDTO(
        @JsonProperty("email")
        @NotEmpty(message = "{email.notempty}")
        @Email(message = "{email.invalid}")
        String email,

        @NotEmpty(message = "{telnum.notempty}")
        @NotEmpty(message = "{telnum.notempty}")
        @JsonProperty("telnum") String telnum,

        @NotEmpty(message = "{postcode.notempty}")
        @NotNull(message = "{postcode.notempty}")
        @JsonProperty("postcode") String postcode,

        @NotNull(message = "{vendor.type.notnull}")
        @JsonProperty("vendorType") VendorTypeEnum vendorType,

        @NotEmpty(message = "{services.offered.notnull}")
        @JsonProperty(value = "servicesOffered", access = JsonProperty.Access.WRITE_ONLY)
        List<Integer> servicesOffered,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<ServiceOfferedDTO> services

) implements WaitlistRequestDTO {
    public WaitlistServiceProviderRequestDTO {
        postcode = (null == postcode) ? null : postcode.toUpperCase();
    }
}
