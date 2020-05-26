package com.southsystem.cooperativeassembly.exceptions;

import java.time.Duration;
import java.time.LocalDateTime;

public class VotingSessionExpiredException extends Exception {
    public VotingSessionExpiredException(LocalDateTime expires) {
        super("Session expired " + Duration.between(expires, LocalDateTime.now()).toString() + "ago");
    }
}
