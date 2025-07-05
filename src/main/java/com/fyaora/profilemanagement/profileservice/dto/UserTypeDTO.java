package com.fyaora.profilemanagement.profileservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "{user.type.not.null}")
    private UserTypeEnum type;

    @JsonProperty("description")
    @NotBlank(message = "{user.description.not.blank}")
    private String description;

    @JsonProperty("enabled")
    private boolean enabled;
}