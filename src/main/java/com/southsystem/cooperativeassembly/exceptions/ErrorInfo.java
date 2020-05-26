package com.southsystem.cooperativeassembly.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {
    private String uri;
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private Map<String, String> details;
    private String detail;
}
