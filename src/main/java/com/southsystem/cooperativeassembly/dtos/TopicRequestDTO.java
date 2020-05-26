package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class TopicRequestDTO {
    @NotNull
    @NotEmpty
    private String topic;
    private String description;
}
