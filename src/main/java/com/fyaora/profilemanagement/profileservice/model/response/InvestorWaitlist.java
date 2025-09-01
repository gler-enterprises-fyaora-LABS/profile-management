package com.fyaora.profilemanagement.profileservice.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fyaora.profilemanagement.profileservice.model.request.WaitlistRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(toBuilder = true)
public record InvestorWaitlist(
        @JsonProperty("email")
        @NotEmpty(message = "{email.notempty}")
        @Email(message = "{email.invalid}")
        String email,

        @JsonProperty("name")
        @NotEmpty(message = "{name.notempty}")
        String name,

        @JsonProperty("telnum")
        @Pattern(
                regexp = "^$|^\\+?[0-9\\s\\-()]{7,20}$",
                message = "{telnum.invalid}"
        )
        String telnum

) implements WaitlistRequest { }
