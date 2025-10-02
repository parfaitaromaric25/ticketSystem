package com.example.ticket.service;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.dto.UserDTO;
import com.example.ticket.service.generic.GenericService;

import java.util.List;

public interface UserService extends GenericService<UserDTO, Long> {
    List<TicketDTO> getUserTickets(Long userId);
    UserDTO findByUsername(String username);
}