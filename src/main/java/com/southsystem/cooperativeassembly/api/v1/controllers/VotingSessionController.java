package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.dtos.VotingSessionReportDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionNotValidException;
import com.southsystem.cooperativeassembly.services.VotingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voting-sessions")
public class VotingSessionController {
    @Autowired
    private VotingSessionService service;

    @GetMapping
    public ResponseEntity<List<VotingSessionResponseDTO>> list() {
        List<VotingSessionResponseDTO> sessions = service.getAllSessions();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<VotingSessionResponseDTO> get(@PathVariable Long id) throws VotingSessionNotFoundException {
        VotingSessionResponseDTO session = service.getSession(id);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}/report")
    public ResponseEntity<VotingSessionReportDTO> report(@PathVariable Long id) throws VotingSessionNotFoundException {
        VotingSessionReportDTO report = service.generateReport(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<VotingSessionResponseDTO> create(@RequestBody final VotingSessionRequestDTO request) throws VotingSessionNotValidException {
        VotingSessionResponseDTO response = service.openSession(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
