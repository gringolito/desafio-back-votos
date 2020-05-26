package com.southsystem.cooperativeassembly.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicResponseDTO {
    private Long id;
    private String topic;
    private String description;
    private LocalDateTime created;
}
