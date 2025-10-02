package com.example.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.ticket.dto.UserDTO;
import com.example.ticket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UserService userService;
    
    private UserDTO userDTO;
    
    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .build();
    }
    
    @Test
    @WithMockUser
    void testGetAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(Arrays.asList(userDTO));
        
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("testuser"));
    }
    
    @Test
    @WithMockUser
    void testGetUserById_Success() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(userDTO));
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    @WithMockUser
    void testGetUserById_NotFound() throws Exception {
        when(userService.findById(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateUser() throws Exception {
        when(userService.create(any(UserDTO.class))).thenReturn(userDTO);
        
        mockMvc.perform(post("/api/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    @WithMockUser
    void testUpdateUser() throws Exception {
        when(userService.update(eq(1L), any(UserDTO.class))).thenReturn(userDTO);
        
        mockMvc.perform(put("/api/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    @WithMockUser
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }
}
