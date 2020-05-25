package com.southsystem.cooperativeassembly.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VoteResponseDTO {
    private Long id;
    private String cpf;
    private String vote;
    private Long votingSessionId;
}
