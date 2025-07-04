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
public class UserTypeResponseDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private UserTypeEnum type;

    @JsonProperty("status")
    private UserTypeStatus status;
}