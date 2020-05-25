package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.southsystem.cooperativeassembly.models.Topic;
import com.southsystem.cooperativeassembly.services.TopicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TopicController.class)
public class TopicControllerUnitTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TopicService service;

    @Test
    public void listOk() throws Exception {
        mvc.perform(get("/api/v1/topics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).getAllTopics();
    }

    @Test(expected = Exception.class)
    public void listFail() throws Exception {
        doThrow(Exception.class).when(service).getAllTopics();

        mvc.perform(get("/api/v1/topics"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void getOk() throws Exception {
        Topic topic = new Topic();
        topic.setTopic_id(Long.valueOf(1));
        topic.setTopic("Director Salary Raise");
        topic.setDate(LocalDateTime.of(2020, 03, 04, 12, 01, 02, 03));
        doReturn(topic).when(service).getTopic(Long.valueOf(1));

        mvc.perform(get("/api/v1/topics/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("topic_id").value(topic.getTopic_id()))
                .andExpect(jsonPath("topic").value(topic.getTopic()))
                .andExpect(jsonPath("description").value(topic.getDescription()))
                .andExpect(jsonPath("date").value(topic.getDate().toString()));

        verify(service, times(1)).getTopic(Long.valueOf(1));
    }

    @Test(expected = Exception.class)
    public void getFail() throws Exception {
        doThrow(Exception.class).when(service).getTopic(Long.valueOf(1));

        mvc.perform(get("/api/v1/topics/1"))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).getTopic(Long.valueOf(1));
    }

    @Test
    public void getNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(service).getTopic(Long.valueOf(1));

        mvc.perform(get("/api/v1/topics/1"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getTopic(Long.valueOf(1));
    }

    @Test
    public void createOk() throws Exception {
        Topic response = new Topic();
        response.setTopic_id(Long.valueOf(1));
        response.setTopic("Director Salary Raise");
        response.setDate(LocalDateTime.of(2020, 03, 04, 12, 01, 02, 03));

        doReturn(response).when(service).createTopic(any(Topic.class));

        Topic request = new Topic();
        request.setDescription("Director Salary Raise");
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("topic_id").value(response.getTopic_id()))
                .andExpect(jsonPath("topic").value(response.getTopic()))
                .andExpect(jsonPath("description").value(response.getDescription()))
                .andExpect(jsonPath("date").value(response.getDate().toString()));

        verify(service, times(1)).createTopic(any(Topic.class));
    }

    @Test(expected = Exception.class)
    public void createFail() throws Exception {
        doThrow(Exception.class).when(service).createTopic(any(Topic.class));

        Topic request = new Topic();
        request.setDescription("Intern Salary Raise");
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is5xxServerError());

        verify(service, times(1)).createTopic(any(Topic.class));
    }

    @Test
    public void createBadRequest() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(service).createTopic(any(Topic.class));

        Topic request = new Topic();
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).createTopic(any(Topic.class));
    }
}
