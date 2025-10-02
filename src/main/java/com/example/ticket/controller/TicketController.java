package com.example.ticket.controller;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.exception.UnauthorizedException;
import com.example.ticket.model.enums.TicketStatus;
import com.example.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "API de gestion des tickets")
public class TicketController {
    
    private final TicketService ticketService;
    
    @GetMapping
    @Operation(summary = "Récupérer tous les tickets")
    @ApiResponse(responseCode = "200", description = "Liste des tickets récupérée")
    public ResponseEntity<List<TicketDTO>> getAllTickets(
            Authentication authentication,
            @RequestParam(required = false) TicketStatus status) {
        
        // Contrôle d'accès: admin voit tous les tickets, user voit seulement les siens
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if (status != null) {
                return ResponseEntity.ok(ticketService.findByStatus(status));
            }
            return ResponseEntity.ok(ticketService.findAll());
        } else {
            String username = authentication.getName();
            return ResponseEntity.ok(ticketService.findByAssignedUser(username));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un ticket par son ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ticket trouvé"),
        @ApiResponse(responseCode = "404", description = "Ticket non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    public ResponseEntity<TicketDTO> getTicketById(
            @PathVariable Long id,
            Authentication authentication) {
        
        return ticketService.findById(id)
            .map(ticket -> {
                // Vérifier que l'utilisateur a le droit d'accéder à ce ticket
                if (!hasAccessToTicket(ticket, authentication)) {
                    throw new UnauthorizedException(
                        "Vous n'avez pas accès à ce ticket");
                }
                return ResponseEntity.ok(ticket);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ticket créé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<TicketDTO> createTicket(
            @Valid @RequestBody TicketDTO ticketDTO) {
        TicketDTO created = ticketService.create(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ticket mis à jour"),
        @ApiResponse(responseCode = "404", description = "Ticket non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    public ResponseEntity<TicketDTO> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketDTO ticketDTO,
            Authentication authentication) {
        
        // Vérifier l'accès avant la mise à jour
        ticketService.findById(id).ifPresent(ticket -> {
            if (!hasAccessToTicket(ticket, authentication)) {
                throw new UnauthorizedException(
                    "Vous n'avez pas le droit de modifier ce ticket");
            }
        });
        
        TicketDTO updated = ticketService.update(id, ticketDTO);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}/assign/{userId}")
    @Operation(summary = "Assigner un ticket à un utilisateur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ticket assigné"),
        @ApiResponse(responseCode = "404", description = "Ticket ou utilisateur non trouvé")
    })
    public ResponseEntity<TicketDTO> assignTicket(
            @PathVariable Long id,
            @PathVariable Long userId) {
        TicketDTO assigned = ticketService.assignTicket(id, userId);
        return ResponseEntity.ok(assigned);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un ticket")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ticket supprimé"),
        @ApiResponse(responseCode = "404", description = "Ticket non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long id,
            Authentication authentication) {
        
        // Seul un admin peut supprimer
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException(
                "Seul un administrateur peut supprimer un ticket");
        }
        
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Méthode utilitaire pour vérifier l'accès
    private boolean hasAccessToTicket(TicketDTO ticket, Authentication auth) {
        // Admin a accès à tout
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        // User a accès seulement à ses tickets
        String username = auth.getName();
        return ticket.getAssignedUsername() != null && 
               ticket.getAssignedUsername().equals(username);
    }
}