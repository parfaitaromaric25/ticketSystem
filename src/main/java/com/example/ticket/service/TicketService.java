package com.example.ticket.service;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.model.enums.TicketStatus;
import com.example.ticket.service.generic.GenericService;

import java.util.List;

public interface TicketService extends GenericService<TicketDTO, Long> {
    TicketDTO assignTicket(Long ticketId, Long userId);
    List<TicketDTO> findByStatus(TicketStatus status);
    List<TicketDTO> findByAssignedUser(String username);
}