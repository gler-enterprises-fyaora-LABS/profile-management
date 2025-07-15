package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WaitlistSearchDTO(
        @JsonProperty("email") String email,
        @JsonProperty("telnum") String telnum
) { }
