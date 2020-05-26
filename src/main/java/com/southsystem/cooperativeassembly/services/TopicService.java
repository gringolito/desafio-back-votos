package com.southsystem.cooperativeassembly.services;

import com.southsystem.cooperativeassembly.converters.TopicConverter;
import com.southsystem.cooperativeassembly.dtos.TopicRequestDTO;
import com.southsystem.cooperativeassembly.dtos.TopicResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.TopicNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.TopicNotValidException;
import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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

    public TopicResponseDTO getTopic(Long id) throws TopicNotFoundException {
        Topic topic = repository.findById(id).orElse(null);
        if (topic == null) {
            throw new TopicNotFoundException(id);
        }

        return converter.toResponseDTO(topic);
    }

    public TopicResponseDTO createTopic(TopicRequestDTO request) throws TopicNotValidException {
        validateTopic(request);

        Topic topic = converter.toModel(request);
        setCreationDate(topic);
        try {
            topic = repository.saveAndFlush(topic);
        } catch (EntityExistsException ex) {
            throw new TopicNotValidException("Topic already exists");
        }
        return converter.toResponseDTO(topic);
    }

    private void setCreationDate(Topic topic) {
        topic.setCreated(LocalDateTime.now());
    }

    private void validateTopic(TopicRequestDTO request) throws TopicNotValidException {
        if (request.getTopic() == null || request.getTopic().isEmpty()) {
            throw new TopicNotValidException("Missing field topic");
        }
    }
}
