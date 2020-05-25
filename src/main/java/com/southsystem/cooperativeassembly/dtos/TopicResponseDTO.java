package com.southsystem.cooperativeassembly.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TopicResponseDTO {
    private Long id;
    private String topic;
    private String description;
    private LocalDateTime created;
}
