package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.models.VotingSession;
import com.southsystem.cooperativeassembly.repositories.VoteRepository;
import lombok.Data;
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
    @Data
    private class AssociateStatus implements Serializable {
        private String status;
    }

    @Autowired
    private VoteRepository repository;

    private RestTemplate restTemplate;

    @Value("${app.vote.cpf-validator-url}")
    private String cpfValidatorUrl;

    public VoteService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public List<Vote> getAllVotes() {
        return repository.findAll();
    }

    public Vote getVote(Long id) {
        Vote vote = repository.getOne(id);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return vote;
    }

    public Vote addVote(Vote vote) {
        validateVotingSession(vote);
        validateAssociate(vote);
        return repository.saveAndFlush(vote);
    }

    public Long getYesVotesBySession(VotingSession session) {
        return repository.countVotesByVotingSessionAndVote(session, "Yes");
    }

    public Long getNoVotesBySession(VotingSession session) {
        return repository.countVotesByVotingSessionAndVote(session, "No");
    }

    private void validateAssociate(Vote vote) {
        if (repository.findFirstByVotingSessionAndAssociateCpf(vote.getVotingSession(), vote.getAssociateCpf()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Associate has already voted in this session.");
        }

        String url = cpfValidatorUrl + "/" + vote.getAssociateCpf();
        ResponseEntity<AssociateStatus> response = restTemplate.getForEntity(url, AssociateStatus.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            if (response.getBody().getStatus() != "ABLE_TO_VOTE") {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid associate: " + vote.getAssociateCpf());
        }
    }

    private void validateVotingSession(Vote vote) {
        if (vote.getVotingSession() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid voting session.");
        }
        if (vote.getVotingSession().getExpires().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Voting session closed.");
        }
    }
}
