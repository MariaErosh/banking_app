package com.pioner.banking.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pioner.banking.TestContainerConfig;
import com.pioner.banking.api.dto.EmailAddRequest;
import com.pioner.banking.api.dto.EmailUpdateRequest;
import com.pioner.banking.api.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestContainerConfig.Initializer.class)

class EmailControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldAddEmailToUser() throws Exception {
        String token = "Bearer " + jwtUtil.generateToken(1L);
        EmailAddRequest request = new EmailAddRequest("test@example.com");

        mockMvc.perform(post("/api/users/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailIfValidationError() throws Exception {
        String token = "Bearer " + jwtUtil.generateToken(1L);
        EmailUpdateRequest request = new EmailUpdateRequest("ivan@example.com", null);

        mockMvc.perform(put("/api/users/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("must not be empty"));
    }

}