package com.southsystem.cooperativeassembly.exceptions;

public class TopicNotFoundException extends Exception {
    public TopicNotFoundException(Long id) {
        super("Invalid Topic ID: " + id.toString());
    }
}
