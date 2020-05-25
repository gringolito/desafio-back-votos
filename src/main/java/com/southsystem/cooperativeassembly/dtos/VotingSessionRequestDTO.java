package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VotingSessionRequestDTO {
    private Long topicId;
    private LocalDateTime expires;
}
