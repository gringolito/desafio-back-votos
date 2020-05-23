package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.models.VotingSession;
import com.southsystem.cooperativeassembly.repositories.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voting-sessions")
public class VotingSessionController {
    @Autowired
    private VotingSessionRepository repository;

    @GetMapping
    public List<VotingSession> list() {
        return repository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public VotingSession get(@PathVariable Long id) {
        return repository.getOne(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VotingSession create(@RequestBody final VotingSession votingSession) {
        return repository.saveAndFlush(votingSession);
    }
}
