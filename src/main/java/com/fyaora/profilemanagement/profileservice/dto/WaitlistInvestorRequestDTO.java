package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(toBuilder = true)
public record WaitlistInvestorRequestDTO(
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

) implements WaitlistRequestDTO { }
