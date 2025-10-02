package com.example.ticket.service;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.dto.mapper.TicketMapper;
import com.example.ticket.exception.ResourceNotFoundException;
import com.example.ticket.model.entity.Ticket;
import com.example.ticket.model.entity.User;
import com.example.ticket.model.enums.TicketStatus;
import com.example.ticket.repository.TicketRepository;
import com.example.ticket.repository.UserRepository;
import com.example.ticket.service.generic.GenericServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl extends GenericServiceImpl<TicketDTO, Long> implements TicketService {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;
    
    @Override
    protected JpaRepository<TicketDTO, Long> getRepository() {
        return null;
    }
    
    @Override
    protected String getEntityName() {
        return "Ticket";
    }
    
    @Override
    public TicketDTO create(TicketDTO dto) {
        log.info("Création d'un nouveau ticket: {}", dto.getTitle());
        Ticket ticket = ticketMapper.toEntity(dto);
        
        if (dto.getAssignedUserId() != null) {
            User user = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Utilisateur avec l'ID " + dto.getAssignedUserId() + " non trouvé"));
            ticket.setAssignedUser(user);
        }
        
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(savedTicket);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TicketDTO> findById(Long id) {
        log.debug("Recherche du ticket avec l'ID: {}", id);
        return ticketRepository.findById(id)
            .map(ticketMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> findAll() {
        log.debug("Récupération de tous les tickets");
        return ticketRepository.findAll().stream()
            .map(ticketMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public TicketDTO update(Long id, TicketDTO dto) {
        log.info("Mise à jour du ticket avec l'ID: {}", id);
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Ticket avec l'ID " + id + " non trouvé"));
        
        ticketMapper.updateEntityFromDTO(dto, ticket);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(updatedTicket);
    }
    
    @Override
    public void delete(Long id) {
        log.info("Suppression du ticket avec l'ID: {}", id);
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Ticket avec l'ID " + id + " non trouvé");
        }
        ticketRepository.deleteById(id);
    }
    
    @Override
    public TicketDTO assignTicket(Long ticketId, Long userId) {
        log.info("Assignation du ticket {} à l'utilisateur {}", ticketId, userId);
        
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Ticket avec l'ID " + ticketId + " non trouvé"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Utilisateur avec l'ID " + userId + " non trouvé"));
        
        ticket.setAssignedUser(user);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(updatedTicket);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> findByStatus(TicketStatus status) {
        log.debug("Recherche des tickets avec le statut: {}", status);
        return ticketRepository.findByStatus(status).stream()
            .map(ticketMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> findByAssignedUser(String username) {
        log.debug("Recherche des tickets assignés à: {}", username);
        return ticketRepository.findByAssignedUsername(username).stream()
            .map(ticketMapper::toDTO)
            .collect(Collectors.toList());
    }
}