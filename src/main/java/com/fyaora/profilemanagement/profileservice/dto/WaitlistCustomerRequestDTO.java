package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record WaitlistCustomerRequestDTO(
        @JsonProperty("email")
        @NotEmpty(message = "{email.notempty}")
        @Email(message = "{email.invalid}")
        String email,

        @JsonProperty("telnum") String telnum,
        @JsonProperty("postcode") String postcode

) implements WaitlistRequestDTO {
        public WaitlistCustomerRequestDTO {
                postcode = (null == postcode ? null : postcode.toUpperCase());
        }
}
