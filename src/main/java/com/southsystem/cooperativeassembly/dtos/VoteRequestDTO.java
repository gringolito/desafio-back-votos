package com.southsystem.cooperativeassembly.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class VoteRequestDTO {
    @NotNull
    @Pattern(regexp = "^\\d{9}-\\d{2}$", message = "must be in format 000000000-00")
    private String cpf;
    @NotNull
    @Pattern(regexp = "Sim|Não", message = "must be 'Sim' or 'Não'")
    private String vote;
    @NotNull
    @Positive
    private Long votingSessionId;
}
