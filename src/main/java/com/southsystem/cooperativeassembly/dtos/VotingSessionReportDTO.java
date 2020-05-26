package com.southsystem.cooperativeassembly.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class VotingSessionReportDTO {
    private TopicResponseDTO topic;
    private Boolean expired;
    private Long yes;
    private Long no;
    private List<VoteResponseDTO> votes;
}
