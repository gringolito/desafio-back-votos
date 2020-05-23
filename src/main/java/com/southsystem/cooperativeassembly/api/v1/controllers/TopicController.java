package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {
    @Autowired
    private TopicRepository repository;

    @GetMapping
    public List<Topic> list() {
        return repository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Topic get(@PathVariable Long id) {
        return repository.getOne(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Topic create(@RequestBody final Topic topic) {
        return repository.saveAndFlush(topic);
    }
}
