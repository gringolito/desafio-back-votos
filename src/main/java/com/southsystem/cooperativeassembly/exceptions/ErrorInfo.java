package com.southsystem.cooperativeassembly.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorInfo {
    private String message;
    private String detail;
    private LocalDateTime timestamp;
    private String uri;
}
