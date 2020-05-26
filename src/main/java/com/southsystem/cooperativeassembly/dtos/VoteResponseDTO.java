package com.southsystem.cooperativeassembly.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteResponseDTO {
    private Long id;
    private String cpf;
    private String vote;
    private Long votingSessionId;
}
