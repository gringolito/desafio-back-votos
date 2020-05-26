package com.southsystem.cooperativeassembly.exceptions;

import com.southsystem.cooperativeassembly.models.VotingSession;

import java.time.Duration;
import java.time.LocalDateTime;

public class VotingSessionExpiredException extends Exception {
    public VotingSessionExpiredException(VotingSession session) {
        super("Session expired " + Duration.between(session.getExpires(), LocalDateTime.now()).toString() + " ago");
    }
}
