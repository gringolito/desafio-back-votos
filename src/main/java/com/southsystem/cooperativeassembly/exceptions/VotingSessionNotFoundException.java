package com.southsystem.cooperativeassembly.exceptions;

public class VotingSessionNotFoundException extends Exception {
    public VotingSessionNotFoundException(Long id) {
        super("Invalid Voting Session ID: " + id.toString());
    }
}
