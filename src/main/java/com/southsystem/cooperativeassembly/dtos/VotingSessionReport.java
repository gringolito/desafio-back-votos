package com.southsystem.cooperativeassembly.dtos;

import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.models.Vote;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class VotingSessionReport {
    @NotNull
    private Topic topic;

    @NotNull
    private Boolean expired;

    @NotNull
    private Long yes;

    @NotNull
    private Long no;

    @NotNull
    private List<Vote> votes;
}
