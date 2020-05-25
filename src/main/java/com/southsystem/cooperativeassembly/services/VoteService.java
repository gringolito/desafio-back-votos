package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.converters.VoteConverter;
import com.southsystem.cooperativeassembly.dtos.VoteRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VoteResponseDTO;
import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.models.VotingSession;
import com.southsystem.cooperativeassembly.repositories.VoteRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteConverter converter;

    @Autowired
    private VotingSessionService sessionService;

    private final RestTemplate restTemplate;

    @Value("${app.vote.cpf-validator-url}")
    private String cpfValidatorUrl;

    public VoteService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public List<VoteResponseDTO> getAllVotes() {
        return converter.toResponseDTO(repository.findAll());
    }

    public VoteResponseDTO getVote(Long id) {
        Vote vote = repository.getOne(id);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return converter.toResponseDTO(vote);
    }

    public VoteResponseDTO addVote(VoteRequestDTO request) {
        validateVotingSession(request);
        validateAssociate(request);
        Vote vote = converter.toModel(request);
        return converter.toResponseDTO(repository.saveAndFlush(vote));
    }

    public Long getYesVotesBySession(VotingSession session) {
        return repository.countVotesByVotingSessionAndVote(session, "Yes");
    }

    public Long getNoVotesBySession(VotingSession session) {
        return repository.countVotesByVotingSessionAndVote(session, "No");
    }

    private void validateAssociate(VoteRequestDTO request) {
        VotingSession session = sessionService.getVotingSession(request.getVotingSessionId());
        if (repository.findFirstByVotingSessionAndCpf(session, request.getCpf()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Associate has already voted in this session.");
        }

        String url = cpfValidatorUrl + "/" + request.getCpf();
        ResponseEntity<AssociateStatus> response = restTemplate.getForEntity(url, AssociateStatus.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            if (!response.getBody().getStatus().equals("ABLE_TO_VOTE")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CPF: " + request.getCpf());
        }
    }

    private void validateVotingSession(VoteRequestDTO request) {
        if (request.getVotingSessionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid voting session.");
        }

        VotingSession session = sessionService.getVotingSession(request.getVotingSessionId());
        if (session.getExpires().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Voting session closed.");
        }
    }

    @Data
    @NoArgsConstructor
    private static class AssociateStatus implements Serializable {
        private String status;
    }
}
