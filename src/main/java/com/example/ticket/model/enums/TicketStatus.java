package com.example.ticket.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statut d'un ticket")
public enum TicketStatus {
    
    EN_COURS("En cours"),
    TERMINE("Terminé"),
    ANNULE("Annulé");
    
    private final String displayName;
    
    TicketStatus(String displayName) {
        this.displayName = displayName;
    }
    
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}