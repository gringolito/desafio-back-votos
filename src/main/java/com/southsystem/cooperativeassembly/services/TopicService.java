package com.southsystem.cooperativeassembly.services;

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

    public List<Topic> getAllTopics() {
        return repository.findAll();
    }

    public Topic getTopic(Long id) {
        Topic topic = repository.getOne(id);
        if (topic == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return topic;
    }

    public Topic createTopic(Topic topic) {
        validateTopic(topic);
        setCreationDate(topic);
        return repository.saveAndFlush(topic);
    }

    private void setCreationDate(Topic topic) {
        topic.setDate(LocalDateTime.now());
    }

    private void validateTopic(Topic topic) {
        if (topic.getTopic() == null || topic.getTopic().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic.");
        }
    }
}
