package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeDTO {
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("description")
    private String description;
    @JsonProperty("enabled")
    private Boolean enabled;
}