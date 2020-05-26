package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class TopicRequestDTO {
    @NotEmpty
    private String topic;
    private String description;
}
