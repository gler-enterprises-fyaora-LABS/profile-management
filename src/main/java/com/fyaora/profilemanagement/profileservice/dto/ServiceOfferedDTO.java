package com.fyaora.profilemanagement.profileservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ServiceOfferedDTO {
    private Integer id;
    private String name;
    private String description;
}
