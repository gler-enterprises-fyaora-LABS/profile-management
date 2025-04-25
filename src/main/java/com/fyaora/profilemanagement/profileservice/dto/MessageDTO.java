package com.fyaora.profilemanagement.profileservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDTO {
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error")
    private String error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("path")
    private String path;
}
