package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {
    @Autowired
    private TopicService service;

    @GetMapping
    public List<Topic> list() {
        return service.getAllTopics();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Topic get(@PathVariable Long id) {
        return service.getTopic(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Topic create(@RequestBody final Topic topic) {
        return service.createTopic(topic);
    }
}
