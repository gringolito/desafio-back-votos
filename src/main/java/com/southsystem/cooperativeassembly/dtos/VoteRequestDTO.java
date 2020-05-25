package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteRequestDTO {
    private String cpf;
    private String vote;
    private Long votingSessionId;
}
