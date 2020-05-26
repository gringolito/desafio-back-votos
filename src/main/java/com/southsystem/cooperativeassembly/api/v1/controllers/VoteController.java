package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.dtos.VoteRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VoteResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.VoteNotAuthorizedException;
import com.southsystem.cooperativeassembly.exceptions.VoteNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.VoteNotValidException;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionExpiredException;
import com.southsystem.cooperativeassembly.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {
    @Autowired
    private VoteService service;

    @GetMapping
    public ResponseEntity<List<VoteResponseDTO>> list() {
        List<VoteResponseDTO> votes = service.getAllVotes();
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<VoteResponseDTO> get(@PathVariable Long id) throws VoteNotFoundException {
        VoteResponseDTO vote = service.getVote(id);
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VoteResponseDTO> create(@RequestBody final VoteRequestDTO request) throws VoteNotAuthorizedException, VoteNotValidException, VotingSessionExpiredException {
        VoteResponseDTO response = service.addVote(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
