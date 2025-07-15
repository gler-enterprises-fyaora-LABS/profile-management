package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record WaitlistInvestorRequestDTO(
        @JsonProperty("email")
        @NotEmpty(message = "{email.notempty}")
        @Email(message = "{email.invalid}")
        String email,

        @JsonProperty("name")
        @NotEmpty(message = "{name.notempty}")
        @NotNull(message = "{name.notempty}")
        String name,

        @JsonProperty("telnum") String telnum

) implements WaitlistRequestDTO { }
