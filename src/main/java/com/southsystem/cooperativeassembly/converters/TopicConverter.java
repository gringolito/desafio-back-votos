package com.southsystem.cooperativeassembly.converters;

import com.southsystem.cooperativeassembly.dtos.TopicRequestDTO;
import com.southsystem.cooperativeassembly.dtos.TopicResponseDTO;
import com.southsystem.cooperativeassembly.models.Topic;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TopicConverter {
    public Topic toModel(TopicRequestDTO dto) {
        Topic model = new Topic();
        model.setTopic(dto.getTopic());
        model.setDescription(dto.getDescription());
        return model;
    }

    public TopicResponseDTO toResponseDTO(Topic model) {
        return TopicResponseDTO.builder()
                .id(model.getTopicId())
                .topic(model.getTopic())
                .description(model.getDescription())
                .created(model.getCreated())
                .build();
    }

    public List<TopicResponseDTO> toResponseDTO(List<Topic> model) {
        List<TopicResponseDTO> dto = new ArrayList<>();
        model.forEach(topic -> {
            dto.add(toResponseDTO(topic));
        });
        return dto;
    }
}
