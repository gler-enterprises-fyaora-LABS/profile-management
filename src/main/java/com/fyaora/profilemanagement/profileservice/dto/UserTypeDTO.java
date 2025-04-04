package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fyaora.profilemanagement.profileservice.model.db.entity.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("type")
    private UserTypeEnum type;
    @JsonProperty("description")
    private String description;
    @JsonProperty("enabled")
    private Boolean enabled;
}