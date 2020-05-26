package com.southsystem.cooperativeassembly.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotingSessionResponseDTO {
    private Long id;
    private TopicResponseDTO topic;
    private LocalDateTime expires;
}
