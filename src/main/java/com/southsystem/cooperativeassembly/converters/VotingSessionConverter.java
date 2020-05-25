package com.southsystem.cooperativeassembly.converters;

import com.southsystem.cooperativeassembly.dtos.VotingSessionRequestDTO;
import com.southsystem.cooperativeassembly.dtos.VotingSessionResponseDTO;
import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.models.VotingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VotingSessionConverter {
    @Autowired
    private TopicConverter topicConverter;

    public VotingSession toModel(VotingSessionRequestDTO dto) {
        VotingSession model = new VotingSession();
        model.setExpires(dto.getExpires());

        Topic topic = new Topic();
        topic.setTopicId(dto.getTopicId());
        model.setTopic(topic);

        return model;
    }

    public VotingSessionResponseDTO toResponseDTO(VotingSession model) {
        return VotingSessionResponseDTO.builder()
                .id(model.getVotingSessionId())
                .topic(topicConverter.toResponseDTO(model.getTopic()))
                .expires(model.getExpires())
                .build();
    }

    public List<VotingSessionResponseDTO> toResponseDTO(List<VotingSession> model) {
        List<VotingSessionResponseDTO> dto = new ArrayList<>();
        model.forEach(session -> {
            dto.add(toResponseDTO(session));
        });
        return dto;
    }
}
