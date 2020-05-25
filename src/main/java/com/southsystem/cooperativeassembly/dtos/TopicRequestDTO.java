package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicRequestDTO {
    private String topic;
    private String description;
}
