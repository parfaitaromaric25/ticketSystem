package com.example.ticket.dto;

import com.example.ticket.model.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    @NotNull(message = "Le statut est obligatoire")
    private TicketStatus status;
    
    private Long assignedUserId;
    private String assignedUsername;
}