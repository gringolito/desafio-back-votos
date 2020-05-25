package com.southsystem.cooperativeassembly.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class VotingSessionResponseDTO {
    private Long id;
    private TopicResponseDTO topic;
    private LocalDateTime expires;
}
