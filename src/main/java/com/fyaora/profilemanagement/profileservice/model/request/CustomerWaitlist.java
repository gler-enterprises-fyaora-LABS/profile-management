package com.fyaora.profilemanagement.profileservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(toBuilder = true)
public record CustomerWaitlist(
        @JsonProperty("email")
        @NotBlank(message = "{email.notempty}")
        @Email(message = "{email.invalid}")
        String email,

        @JsonProperty("telnum")
        @Pattern(
                regexp = "^$|^\\+?[0-9\\s\\-()]{7,20}$",
                message = "{telnum.invalid}"
        )
        String telnum,

        @JsonProperty("postcode") String postcode

) implements WaitlistRequest {
        public CustomerWaitlist {
                postcode = (null == postcode ? null : postcode.toUpperCase());
        }
}
