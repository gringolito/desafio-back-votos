package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.converters.TopicConverter;
import com.southsystem.cooperativeassembly.converters.VoteConverter;
import com.southsystem.cooperativeassembly.converters.VotingSessionConverter;
import com.southsystem.cooperativeassembly.dtos.VotingSessionReportDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionResponseDTO;
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
    VotingSessionConverter sessionConverter;

    @Autowired
    TopicConverter topicConverter;

    @Autowired
    VoteConverter voteConverter;

    @Autowired
    VoteService voteService;

    public List<VotingSessionResponseDTO> getAllSessions() {
        return sessionConverter.toResponseDTO(repository.findAll());
    }

    public VotingSessionResponseDTO getSession(Long id) {
        VotingSession session = getVotingSession(id);

        return sessionConverter.toResponseDTO(session);
    }

    public VotingSession getVotingSession(Long id) {
        VotingSession session = repository.getOne(id);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return session;
    }

    public VotingSessionReportDTO generateReport(Long id) {
        VotingSession session = getVotingSession(id);
        return VotingSessionReportDTO.builder()
                .topic(topicConverter.toResponseDTO(session.getTopic()))
                .votes(voteConverter.toResponseDTO(session.getVotes()))
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

    public VotingSessionResponseDTO openSession(VotingSessionRequestDTO request) {
        VotingSession session = sessionConverter.toModel(request);
        validateTopic(session);
        validateExpires(session);
        return sessionConverter.toResponseDTO(repository.saveAndFlush(session));
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
