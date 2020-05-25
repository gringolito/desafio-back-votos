package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.converters.TopicConverter;
import com.southsystem.cooperativeassembly.dtos.TopicRequestDTO;
import com.southsystem.cooperativeassembly.dtos.TopicResponseDTO;
import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository repository;

    @Autowired
    private TopicConverter converter;

    public List<TopicResponseDTO> getAllTopics() {
        return converter.toResponseDTO(repository.findAll());
    }

    public TopicResponseDTO getTopic(Long id) {
        Topic topic = repository.getOne(id);
        if (topic == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return converter.toResponseDTO(topic);
    }

    public TopicResponseDTO createTopic(TopicRequestDTO request) {
        Topic topic = converter.toModel(request);
        validateTopic(topic);
        setCreationDate(topic);

        return converter.toResponseDTO(repository.saveAndFlush(topic));
    }

    private void setCreationDate(Topic topic) {
        topic.setCreated(LocalDateTime.now());
    }

    private void validateTopic(Topic topic) {
        if (topic.getTopic() == null || topic.getTopic().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic.");
        }
    }
}
