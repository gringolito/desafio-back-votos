package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.converters.VoteConverter;
import com.southsystem.cooperativeassembly.dtos.VoteRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VoteResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.*;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityExistsException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {
    private final RestTemplate restTemplate;
    @Autowired
    private VoteRepository repository;
    @Autowired
    private VoteConverter converter;
    @Autowired
    private VotingSessionService sessionService;
    @Value("${app.vote.cpf-validator-url}")
    private String cpfValidatorUrl;

    public VoteService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public List<VoteResponseDTO> getAllVotes() {
        return converter.toResponseDTO(repository.findAll());
    }

    public VoteResponseDTO getVote(Long id) throws VoteNotFoundException {
        Vote vote = repository.findById(id).orElse(null);
        if (vote == null) {
            throw new VoteNotFoundException(id);
        }

        return converter.toResponseDTO(vote);
    }

    public VoteResponseDTO addVote(VoteRequestDTO request) throws VoteNotAuthorizedException, VoteNotValidException, VotingSessionExpiredException {
        validateVote(request);

        Vote vote;
        try {
            vote = repository.saveAndFlush(converter.toModel(request));
        } catch (EntityExistsException ex) {
            throw new VoteNotValidException("Vote already exists");
        }
        return converter.toResponseDTO(vote);
    }

    public Long getYesVotesBySession(VotingSession session) {
        return repository.countVotesByVotingSessionAndVote(session, "Sim");
    }

    public Long getNoVotesBySession(VotingSession session) {
        return repository.countVotesByVotingSessionAndVote(session, "Não");
    }

    private void validateVote(VoteRequestDTO request) throws VoteNotAuthorizedException, VoteNotValidException, VotingSessionExpiredException {
        /*
         * Verify if voting session exists and its open
         */
        VotingSession session;
        try {
            session = sessionService.getVotingSession(request.getVotingSessionId());
        } catch (VotingSessionNotFoundException ex) {
            throw new VoteNotValidException("Invalid votingSessionId: " + request.getVotingSessionId());
        }

        if (sessionService.isExpired(session)) {
            throw new VotingSessionExpiredException(session);
        }

        /*
         * Verify if this CFP hasn't yet voted on this session
         */
        if (repository.findFirstByVotingSessionAndCpf(session, request.getCpf()) != null) {
            throw new VoteNotAuthorizedException("This CPF has already voted in this session: " + request.getCpf());
        }

        /*
         * Verify if this CPF is allowed to vote on this session
         */
        String url = cpfValidatorUrl + "/" + request.getCpf();
        ResponseEntity<AssociateStatus> response;
        try {
            response = restTemplate.getForEntity(url, AssociateStatus.class);
        } catch (HttpClientErrorException exception) {
            throw new VoteNotValidException("Invalid CPF: " + request.getCpf());
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            if (!response.getBody().getStatus().equals("ABLE_TO_VOTE")) {
                throw new VoteNotAuthorizedException("This CPF is not able to vote in this session: " + request.getCpf());
            }
        } else {
            throw new VoteNotValidException("Invalid CPF: " + request.getCpf());
        }
    }

    @Data
    @NoArgsConstructor
    private static class AssociateStatus implements Serializable {
        private String status;
    }
}
