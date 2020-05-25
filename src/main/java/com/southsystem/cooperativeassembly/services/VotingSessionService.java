package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.dtos.VotingSessionReport;
import com.southsystem.cooperativeassembly.models.VotingSession;
import com.southsystem.cooperativeassembly.repositories.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotingSessionService {
    @Autowired
    VotingSessionRepository repository;

    @Autowired
    VoteService voteService;

    public List<VotingSession> getAllSessions() {
        return repository.findAll();
    }

    public VotingSession getSession(Long id) {
        VotingSession session = repository.getOne(id);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return session;
    }

    public VotingSessionReport generateReport(Long id) {
        VotingSession session = getSession(id);
        return VotingSessionReport
                .builder()
                .topic(session.getTopic())
                .votes(session.getVotes())
                .expired(session.getExpires().isBefore(LocalDateTime.now()))
                .yes(getYesVotes(session))
                .no(getNoVotes(session))
                .build();
    }

    private Long getYesVotes(VotingSession session) {
        return voteService.getYesVotesBySession(session);
    }

    private Long getNoVotes(VotingSession session) {
        return voteService.getNoVotesBySession(session);
    }

    public VotingSession openSession(VotingSession session) {
        validateTopic(session);
        validateExpires(session);
        return repository.saveAndFlush(session);
    }

    private void validateExpires(VotingSession session) {
        if (session.getExpires() == null) {
            session.setExpires(LocalDateTime.now().plusMinutes(1));
        } else if (session.getExpires().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't create an expired voting session.");
        }
    }

    private void validateTopic(VotingSession session) {
        if (session.getTopic() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic not found.");
        }
    }
}
