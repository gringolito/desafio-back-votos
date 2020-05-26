package com.southsystem.cooperativeassembly.exceptions;

public class VoteNotFoundException extends Exception {
    public VoteNotFoundException(Long id) {
        super("Invalid Vote ID: " + id.toString());
    }
}
