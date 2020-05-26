package com.southsystem.cooperativeassembly.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotingSessionReportDTO {
    private Long id;
    private TopicResponseDTO topic;
    private Boolean expired;
    private Long yes;
    private Long no;
    private List<VoteResponseDTO> votes;
}
