package com.example.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.ticket.dto.TicketDTO;
import com.example.ticket.model.enums.TicketStatus;
import com.example.ticket.service.TicketService;
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

@WebMvcTest(TicketController.class)
class TicketControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private TicketService ticketService;
    
    private TicketDTO ticketDTO;
    
    @BeforeEach
    void setUp() {
        ticketDTO = TicketDTO.builder()
            .id(1L)
            .title("Test Ticket")
            .description("Test Description")
            .status(TicketStatus.EN_COURS)
            .assignedUserId(1L)
            .assignedUsername("testuser")
            .build();
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllTickets_AsAdmin() throws Exception {
        when(ticketService.findAll()).thenReturn(Arrays.asList(ticketDTO));
        
        mockMvc.perform(get("/api/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Test Ticket"));
    }
    
    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetAllTickets_AsUser() throws Exception {
        when(ticketService.findByAssignedUser("testuser"))
            .thenReturn(Arrays.asList(ticketDTO));
        
        mockMvc.perform(get("/api/tickets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Test Ticket"));
    }
    
    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetTicketById_Success() throws Exception {
        when(ticketService.findById(1L)).thenReturn(Optional.of(ticketDTO));
        
        mockMvc.perform(get("/api/tickets/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Ticket"));
    }
    
    @Test
    @WithMockUser
    void testCreateTicket() throws Exception {
        when(ticketService.create(any(TicketDTO.class))).thenReturn(ticketDTO);
        
        mockMvc.perform(post("/api/tickets")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test Ticket"));
    }
    
    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateTicket() throws Exception {
        when(ticketService.findById(1L)).thenReturn(Optional.of(ticketDTO));
        when(ticketService.update(eq(1L), any(TicketDTO.class))).thenReturn(ticketDTO);
        
        mockMvc.perform(put("/api/tickets/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Ticket"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAssignTicket() throws Exception {
        when(ticketService.assignTicket(1L, 1L)).thenReturn(ticketDTO);
        
        mockMvc.perform(put("/api/tickets/1/assign/1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.assignedUserId").value(1));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTicket_AsAdmin() throws Exception {
        mockMvc.perform(delete("/api/tickets/1")
                .with(csrf()))
            .andExpect(status().isNoContent());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void testDeleteTicket_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/tickets/1")
                .with(csrf()))
            .andExpect(status().isForbidden());
    }
}