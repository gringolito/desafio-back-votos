package com.southsystem.cooperativeassembly.converters;

import com.southsystem.cooperativeassembly.dtos.VoteRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VoteResponseDTO;
import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.models.VotingSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VoteConverter {
    public Vote toModel(VoteRequestDTO dto) {
        Vote model = new Vote();
        model.setCpf(dto.getCpf());
        model.setVote(dto.getVote());

        VotingSession session = new VotingSession();
        session.setVotingSessionId(dto.getVotingSessionId());
        model.setVotingSession(session);

        return model;
    }

    public VoteResponseDTO toResponseDTO(Vote model) {
        return VoteResponseDTO.builder()
                .id(model.getVoteId())
                .cpf(model.getCpf())
                .vote(model.getVote())
                .votingSessionId(model.getVotingSession().getVotingSessionId())
                .build();
    }

    public List<VoteResponseDTO> toResponseDTO(List<Vote> model) {
        List<VoteResponseDTO> dto = new ArrayList<>();
        model.forEach(vote -> {
            dto.add(toResponseDTO(vote));
        });
        return dto;
    }
}
