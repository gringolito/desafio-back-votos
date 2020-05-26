package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.converters.TopicConverter;
import com.southsystem.cooperativeassembly.converters.VoteConverter;
import com.southsystem.cooperativeassembly.converters.VotingSessionConverter;
import com.southsystem.cooperativeassembly.dtos.VotingSessionReportDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionNotValidException;
import com.southsystem.cooperativeassembly.models.VotingSession;
import com.southsystem.cooperativeassembly.repositories.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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

    public VotingSessionResponseDTO getSession(Long id) throws VotingSessionNotFoundException {
        VotingSession session = getVotingSession(id);
        return sessionConverter.toResponseDTO(session);
    }

    public VotingSession getVotingSession(Long id) throws VotingSessionNotFoundException {
        VotingSession session = repository.findById(id).orElse(null);
        if (session == null) {
            throw new VotingSessionNotFoundException(id);
        }

        return session;
    }

    public VotingSessionReportDTO generateReport(Long id) throws VotingSessionNotFoundException {
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

    public VotingSessionResponseDTO openSession(VotingSessionRequestDTO request) throws VotingSessionNotValidException {
        validateTopic(request);
        validateExpires(request);

        VotingSession session;
        try {
            session = repository.saveAndFlush(sessionConverter.toModel(request));
        } catch (EntityExistsException ex) {
            throw new VotingSessionNotValidException("Voting Session already exists");
        }
        return sessionConverter.toResponseDTO(session);
    }

    private void validateExpires(VotingSessionRequestDTO request) throws VotingSessionNotValidException {
        if (request.getExpires() == null) {
            request.setExpires(LocalDateTime.now().plusMinutes(1));
        } else if (request.getExpires().isBefore(LocalDateTime.now())) {
            throw new VotingSessionNotValidException("Field expires has already expired");
        }
    }

    private void validateTopic(VotingSessionRequestDTO request) throws VotingSessionNotValidException {
        if (request.getTopicId() == null) {
            throw new VotingSessionNotValidException("Missing field topicId");
        }
    }
}
