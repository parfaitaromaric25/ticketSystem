package com.example.ticket.model.enums;

public enum TicketStatus {

	    EN_COURS("En cours"),
	    TERMINE("Terminé"),
	    ANNULE("Annulé");
	    
	    private final String displayName;
	    
	    TicketStatus(String displayName) {
	        this.displayName = displayName;
	    }
	    
	    public String getDisplayName() {
	        return displayName;
	    }
}
