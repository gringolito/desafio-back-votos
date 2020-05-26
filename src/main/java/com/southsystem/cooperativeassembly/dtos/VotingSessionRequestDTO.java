package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VotingSessionRequestDTO {
    @NotNull
    @Positive
    private Long topicId;
    @Future
    private LocalDateTime expires;
}
