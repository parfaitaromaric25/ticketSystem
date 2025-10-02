package com.example.ticket.dto.mapper;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.model.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    
    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;
        
        return TicketDTO.builder()
            .id(ticket.getId())
            .title(ticket.getTitle())
            .description(ticket.getDescription())
            .status(ticket.getStatus())
            .assignedUserId(ticket.getAssignedUser() != null ? 
                ticket.getAssignedUser().getId() : null)
            .assignedUsername(ticket.getAssignedUser() != null ? 
                ticket.getAssignedUser().getUsername() : null)
            .build();
    }
    
    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;
        
        return Ticket.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .build();
    }
    
    public void updateEntityFromDTO(TicketDTO dto, Ticket ticket) {
        if (dto.getTitle() != null) {
            ticket.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            ticket.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            ticket.setStatus(dto.getStatus());
        }
    }
}