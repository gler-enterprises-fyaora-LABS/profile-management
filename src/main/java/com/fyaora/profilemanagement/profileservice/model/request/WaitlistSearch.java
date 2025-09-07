package com.fyaora.profilemanagement.profileservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record WaitlistSearch(
        @JsonProperty("from") LocalDate from,
        @JsonProperty("to") LocalDate to,
        @JsonProperty("email") String email,
        @JsonProperty("telnum") String telnum,
        @JsonProperty("pageNum") Integer pageNum,
        @JsonProperty("pageSize") Integer pageSize
) { }
