package com.fyaora.typeprovider.typeprovider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTypeDTO {
    private Integer type;
    private String description;
    private Boolean enabled;

    public UserTypeDTO(Integer type, String description, Boolean enabled) {
        this.type = type;
        this.description = description;
        this.enabled = enabled;
    }
}