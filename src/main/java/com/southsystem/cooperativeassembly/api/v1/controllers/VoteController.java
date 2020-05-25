package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
public class VotesController {
    @Autowired
    private VoteRepository repository;

    @GetMapping
    public List<Vote> list() {
        return repository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Vote get(@PathVariable Long id) {
        return repository.getOne(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Vote create(@RequestBody final Vote vote) {
        return repository.saveAndFlush(vote);
    }
}
