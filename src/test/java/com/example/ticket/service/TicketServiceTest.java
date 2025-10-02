package com.example.ticket.service;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.dto.mapper.TicketMapper;
import com.example.ticket.exception.ResourceNotFoundException;
import com.example.ticket.model.entity.Ticket;
import com.example.ticket.model.entity.User;
import com.example.ticket.model.enums.TicketStatus;
import com.example.ticket.repository.TicketRepository;
import com.example.ticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    
    @Mock
    private TicketRepository ticketRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private TicketMapper ticketMapper;
    
    @InjectMocks
    private TicketServiceImpl ticketService;
    
    private Ticket ticket;
    private TicketDTO ticketDTO;
    private User user;
    
    @BeforeEach
    void setUp() {
        user = User.builder()
            //.id()
            .username("testuser")
            .email("test@example.com")
            .build();
        
        ticket = Ticket.builder()
            //.id(1L)
            .title("Test Ticket")
            .description("Test Description")
            .status(TicketStatus.EN_COURS)
            .assignedUser(user)
            .build();
        
        ticketDTO = TicketDTO.builder()
            .id(1L)
            .title("Test Ticket")
            .description("Test Description")
            .status(TicketStatus.EN_COURS)
            .assignedUserId(1L)
            .build();
    }
    
    @Test
    void testCreateTicket() {
        when(ticketMapper.toEntity(any(TicketDTO.class))).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.toDTO(any(Ticket.class))).thenReturn(ticketDTO);
        
        TicketDTO result = ticketService.create(ticketDTO);
        
        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
    
    @Test
    void testAssignTicket_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.toDTO(any(Ticket.class))).thenReturn(ticketDTO);
        
        TicketDTO result = ticketService.assignTicket(1L, 1L);
        
        assertNotNull(result);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
    
    @Test
    void testAssignTicket_TicketNotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.assignTicket(999L, 1L);
        });
    }
    
    @Test
    void testAssignTicket_UserNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.assignTicket(1L, 999L);
        });
    }
    
    @Test
    void testFindByStatus() {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(ticketRepository.findByStatus(TicketStatus.EN_COURS))
            .thenReturn(tickets);
        when(ticketMapper.toDTO(any(Ticket.class))).thenReturn(ticketDTO);
        
        List<TicketDTO> result = ticketService.findByStatus(TicketStatus.EN_COURS);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}