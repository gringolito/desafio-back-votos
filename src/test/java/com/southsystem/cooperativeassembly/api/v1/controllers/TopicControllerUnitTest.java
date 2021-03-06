package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.southsystem.cooperativeassembly.dtos.TopicRequestDTO;
import com.southsystem.cooperativeassembly.dtos.TopicResponseDTO;
import com.southsystem.cooperativeassembly.exceptions.TopicNotFoundException;
import com.southsystem.cooperativeassembly.exceptions.TopicNotValidException;
import com.southsystem.cooperativeassembly.services.TopicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

        verify(service, times(1)).getAllTopics();
    }

    @Test
    public void getOk() throws Exception {
        TopicResponseDTO topic = TopicResponseDTO.builder()
                .id(Long.valueOf(1))
                .topic("Director Salary Raise")
                .created(LocalDateTime.of(2020, 03, 04, 12, 01, 02, 03))
                .build();
        doReturn(topic).when(service).getTopic(Long.valueOf(1));

        mvc.perform(get("/api/v1/topics/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(topic.getId()))
                .andExpect(jsonPath("topic").value(topic.getTopic()))
                .andExpect(jsonPath("created").value(topic.getCreated().toString()));

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
        doThrow(new TopicNotFoundException(Long.valueOf(1))).when(service).getTopic(Long.valueOf(1));

        mvc.perform(get("/api/v1/topics/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/topics/1"))
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Topic Not Found"))
                .andExpect(jsonPath("detail").value("Invalid Topic ID: 1"));

        verify(service, times(1)).getTopic(Long.valueOf(1));
    }

    @Test
    public void createOk() throws Exception {
        TopicResponseDTO response = TopicResponseDTO.builder()
                .id(Long.valueOf(1))
                .topic("Director Salary Raise")
                .created(LocalDateTime.of(2020, 03, 04, 12, 01, 02, 03))
                .build();
        doReturn(response).when(service).createTopic(any(TopicRequestDTO.class));

        TopicRequestDTO request = new TopicRequestDTO();
        request.setTopic("Director Salary Raise");
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(response.getId()))
                .andExpect(jsonPath("topic").value(response.getTopic()))
                .andExpect(jsonPath("created").value(response.getCreated().toString()));

        verify(service, times(1)).createTopic(any(TopicRequestDTO.class));
    }

    @Test
    public void createFail() throws Exception {
        TopicRequestDTO request = new TopicRequestDTO();
        request.setTopic("Intern Salary Raise");
        String json = new ObjectMapper().writeValueAsString(request);
        doThrow(new TopicNotValidException("Topic already exists")).when(service).createTopic(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("uri").value("/api/v1/topics"))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").value("Invalid Topic"))
                .andExpect(jsonPath("detail").value("Topic already exists"));

        verify(service, times(1)).createTopic(request);
    }

    @Test
    public void createValidationNullTopic() throws Exception {
        TopicRequestDTO request = new TopicRequestDTO();
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Request Field-level Validation Failed"))
                .andExpect(jsonPath("details").isNotEmpty())
                .andExpect(jsonPath("details.topic").value("must not be empty, but received: null"));

        verify(service, times(0)).createTopic(any(TopicRequestDTO.class));
    }

    @Test
    public void createValidationEmptyTopic() throws Exception {
        TopicRequestDTO request = new TopicRequestDTO();
        request.setTopic("");
        String json = new ObjectMapper().writeValueAsString(request);

        mvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Request Field-level Validation Failed"))
                .andExpect(jsonPath("details").isNotEmpty())
                .andExpect(jsonPath("details.topic").value("must not be empty, but received: "));

        verify(service, times(0)).createTopic(any(TopicRequestDTO.class));
    }
}
