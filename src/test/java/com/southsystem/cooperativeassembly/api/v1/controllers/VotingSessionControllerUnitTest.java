package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.southsystem.cooperativeassembly.dtos.*;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.VotingSessionNotValidException;
import com.southsystem.cooperativeassembly.services.VotingSessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(VotingSessionController.class)
public class VotingSessionControllerUnitTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private VotingSessionService service;

    @Test
    public void listOk() throws Exception {
        mvc.perform(get("/api/v1/voting-sessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).getAllSessions();
    }

    @Test(expected = Exception.class)
    public void listFail() throws Exception {
        doThrow(Exception.class).when(service).getAllSessions();

        mvc.perform(get("/api/v1/voting-sessions"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getAllSessions();
    }

    @Test
    public void getOk() throws Exception {
        TopicResponseDTO topic = TopicResponseDTO.builder()
                .id(Long.valueOf(7))
                .topic("Director Salary Raise")
                .build();
        VotingSessionResponseDTO session = VotingSessionResponseDTO.builder()
                .id(Long.valueOf(1))
                .topic(topic)
                .expires(LocalDateTime.of(2020, 03, 04, 12, 01, 02, 03))
                .build();
        doReturn(session).when(service).getSession(Long.valueOf(1));

        mvc.perform(get("/api/v1/voting-sessions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(session.getId()))
                .andExpect(jsonPath("expires").value(session.getExpires().toString()))
                .andExpect(jsonPath("topic.id").value(session.getTopic().getId()))
                .andExpect(jsonPath("topic.topic").value(session.getTopic().getTopic()));

        verify(service, times(1)).getSession(Long.valueOf(1));
    }

    @Test(expected = Exception.class)
    public void getFail() throws Exception {
        doThrow(Exception.class).when(service).getSession(Long.valueOf(1));

        mvc.perform(get("/api/v1/voting-sessions/1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getSession(Long.valueOf(1));
    }

    @Test
    public void getNotFound() throws Exception {
        doThrow(new VotingSessionNotFoundException(Long.valueOf(1))).when(service).getSession(Long.valueOf(1));

        mvc.perform(get("/api/v1/voting-sessions/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions/1"))
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Voting Session Not Found"))
                .andExpect(jsonPath("detail").value("Invalid Voting Session ID: 1"));

        verify(service, times(1)).getSession(Long.valueOf(1));
    }

    @Test
    public void createOk() throws Exception {
        TopicResponseDTO topicResponse = TopicResponseDTO.builder()
                .id(Long.valueOf(1))
                .topic("Director Salary Raise")
                .build();
        VotingSessionResponseDTO sessionResponse = VotingSessionResponseDTO.builder()
                .id(Long.valueOf(1))
                .expires(LocalDateTime.of(2020, 03, 04, 12, 01, 02, 03))
                .topic(topicResponse)
                .build();
        doReturn(sessionResponse).when(service).openSession(any(VotingSessionRequestDTO.class));

        VotingSessionRequestDTO request = new VotingSessionRequestDTO();
        request.setTopicId(Long.valueOf(1));
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/voting-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(sessionResponse.getId()))
                .andExpect(jsonPath("expires").value(sessionResponse.getExpires().toString()))
                .andExpect(jsonPath("topic.id").value(sessionResponse.getTopic().getId()))
                .andExpect(jsonPath("topic.topic").value(sessionResponse.getTopic().getTopic()));

        verify(service, times(1)).openSession(any(VotingSessionRequestDTO.class));
    }

    @Test
    public void createFail() throws Exception {
        VotingSessionRequestDTO request = new VotingSessionRequestDTO();
        request.setTopicId(Long.valueOf(1));
        String json = new ObjectMapper().writeValueAsString(request);
        doThrow(new VotingSessionNotValidException("Invalid TopicId: 1")).when(service).openSession(request);

        mvc.perform(post("/api/v1/voting-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions"))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Invalid Voting Session"))
                .andExpect(jsonPath("detail").value("Invalid TopicId: 1"));

        verify(service, times(1)).openSession(request);
    }

    @Test
    public void createValidationNegativeTopicId() throws Exception {
        VotingSessionRequestDTO request = new VotingSessionRequestDTO();
        request.setTopicId(Long.valueOf(-1));
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/voting-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions"))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Request Field-level Validation Failed"))
                .andExpect(jsonPath("details.topicId").value("must be greater than 0, but received: -1"));

        verify(service, times(0)).openSession(any(VotingSessionRequestDTO.class));
    }

    @Test
    public void createValidationNullTopicId() throws Exception {
        VotingSessionRequestDTO request = new VotingSessionRequestDTO();
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/voting-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions"))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Request Field-level Validation Failed"))
                .andExpect(jsonPath("details.topicId").value("must not be null, but received: null"));

        verify(service, times(0)).openSession(any(VotingSessionRequestDTO.class));
    }

    @Test
    public void createValidationExpiresInPast() throws Exception {
        String json = "{\"topicId\":1,\"expires\":\"1000-01-01T00:00:00\"}";

        mvc.perform(post("/api/v1/voting-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions"))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Request Field-level Validation Failed"))
                .andExpect(jsonPath("details.expires").value("must be a future date, but received: 1000-01-01T00:00"));

        verify(service, times(0)).openSession(any(VotingSessionRequestDTO.class));
    }

    @Test
    public void createValidationExpiresInPastNoTopicId() throws Exception {
        String json = "{\"expires\":\"1000-01-01T00:00:00\"}";

        mvc.perform(post("/api/v1/voting-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions"))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Request Field-level Validation Failed"))
                .andExpect(jsonPath("details.expires").value("must be a future date, but received: 1000-01-01T00:00"))
                .andExpect(jsonPath("details.topicId").value("must not be null, but received: null"));

        verify(service, times(0)).openSession(any(VotingSessionRequestDTO.class));
    }

    @Test
    public void reportOk() throws Exception {
        TopicResponseDTO topic = TopicResponseDTO.builder()
                .id(Long.valueOf(1))
                .topic("Director Salary Raise")
                .build();
        List<VoteResponseDTO> votes = new ArrayList<>();
        votes.add(VoteResponseDTO.builder()
                .id(Long.valueOf(1))
                .vote("Sim")
                .cpf("123456789-01")
                .votingSessionId(Long.valueOf(1))
                .build());
        votes.add(VoteResponseDTO.builder()
                .id(Long.valueOf(2))
                .vote("Não")
                .cpf("987654321-10")
                .votingSessionId(Long.valueOf(1))
                .build());
        VotingSessionReportDTO report = VotingSessionReportDTO.builder()
                .id(Long.valueOf(1))
                .expired(Boolean.TRUE)
                .yes(Long.valueOf(7))
                .no(Long.valueOf(4))
                .topic(topic)
                .votes(votes)
                .build();
        doReturn(report).when(service).generateReport(Long.valueOf(1));

        mvc.perform(get("/api/v1/voting-sessions/1/report"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(report.getId()))
                .andExpect(jsonPath("expired").value(report.getExpired()))
                .andExpect(jsonPath("yes").value(report.getYes()))
                .andExpect(jsonPath("no").value(report.getNo()))
                .andExpect(jsonPath("votes", hasSize(report.getVotes().size())))
                .andExpect(jsonPath("topic.id").value(report.getTopic().getId()))
                .andExpect(jsonPath("topic.topic").value(report.getTopic().getTopic()));

        verify(service, times(1)).generateReport(Long.valueOf(1));
    }

    @Test
    public void reportNotFound() throws Exception {
        doThrow(new VotingSessionNotFoundException(Long.valueOf(1))).when(service).generateReport(Long.valueOf(1));

        mvc.perform(get("/api/v1/voting-sessions/1/report"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/voting-sessions/1/report"))
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Voting Session Not Found"))
                .andExpect(jsonPath("detail").value("Invalid Voting Session ID: 1"));

        verify(service, times(1)).generateReport(Long.valueOf(1));
    }
}
