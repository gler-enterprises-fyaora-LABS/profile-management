package com.fyaora.profilemanagement.profileservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WaitlistSearch(
        @JsonProperty("email") String email,
        @JsonProperty("telnum") String telnum,
        @JsonProperty("page") Integer page
) { }
