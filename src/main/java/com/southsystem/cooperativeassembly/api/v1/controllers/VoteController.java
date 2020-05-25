package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {
    @Autowired
    private VoteService service;

    @GetMapping
    public List<Vote> list() {
        return service.getAllVotes();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Vote get(@PathVariable Long id) {
        return service.getVote(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Vote create(@RequestBody final Vote vote) {
        return service.addVote(vote);
    }
}
