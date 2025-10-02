package com.example.ticket.repository;

import com.example.ticket.model.entity.Ticket;
import com.example.ticket.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findByAssignedUserId(Long userId);
    
    List<Ticket> findByStatus(TicketStatus status);
    
    @Query("SELECT t FROM Ticket t WHERE t.assignedUser.username = :username")
    List<Ticket> findByAssignedUsername(@Param("username") String username);
    
    long countByAssignedUserId(Long userId);
}