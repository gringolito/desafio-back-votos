package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.dtos.TopicRequestDTO;
import com.southsystem.cooperativeassembly.dtos.TopicResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.TopicNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.TopicNotValidException;
import com.southsystem.cooperativeassembly.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {
    @Autowired
    private TopicService service;

    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> list() {
        List<TopicResponseDTO> topics = service.getAllTopics();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<TopicResponseDTO> get(@PathVariable Long id) throws TopicNotFoundException {
        TopicResponseDTO topic = service.getTopic(id);
        return new ResponseEntity<>(topic, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TopicResponseDTO> create(@RequestBody final TopicRequestDTO request) throws TopicNotValidException {
        TopicResponseDTO response = service.createTopic(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
