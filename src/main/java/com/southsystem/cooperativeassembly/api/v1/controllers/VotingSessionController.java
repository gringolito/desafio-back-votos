package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.dtos.VotingSessionReport;
import com.southsystem.cooperativeassembly.models.VotingSession;
import com.southsystem.cooperativeassembly.services.VotingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voting-sessions")
public class VotingSessionController {
    @Autowired
    private VotingSessionService service;

    @GetMapping
    public List<VotingSession> list() {
        return service.getAllSessions();
    }

    @GetMapping
    @RequestMapping("{id}")
    public VotingSession get(@PathVariable Long id) {
        return service.getSession(id);
    }

    @GetMapping
    @RequestMapping("{id}/report")
    public VotingSessionReport report(@PathVariable Long id) {
        return service.generateReport(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VotingSession create(@RequestBody final VotingSession votingSession) {
        return service.openSession(votingSession);
    }
}
