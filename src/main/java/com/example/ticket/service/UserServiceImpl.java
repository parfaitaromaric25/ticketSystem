package com.example.ticket.service;

import com.example.ticket.dto.TicketDTO;
import com.example.ticket.dto.UserDTO;
import com.example.ticket.dto.mapper.TicketMapper;
import com.example.ticket.dto.mapper.UserMapper;
import com.example.ticket.exception.ResourceNotFoundException;
import com.example.ticket.model.entity.User;
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
public class UserServiceImpl extends GenericServiceImpl<UserDTO, Long> implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;
    
    @Override
    protected JpaRepository<UserDTO, Long> getRepository() {
        // Cette méthode ne sera pas utilisée car nous surchargeons les méthodes
        return null;
    }
    
    @Override
    protected String getEntityName() {
        return "Utilisateur";
    }
    
    @Override
    public UserDTO create(UserDTO dto) {
        log.info("Création d'un nouvel utilisateur: {}", dto.getUsername());
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Long id) {
        log.debug("Recherche de l'utilisateur avec l'ID: {}", id);
        return userRepository.findById(id)
            .map(userMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        log.debug("Récupération de tous les utilisateurs");
        return userRepository.findAll().stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public UserDTO update(Long id, UserDTO dto) {
        log.info("Mise à jour de l'utilisateur avec l'ID: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Utilisateur avec l'ID " + id + " non trouvé"));
        
        userMapper.updateEntityFromDTO(dto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }
    
    @Override
    public void delete(Long id) {
        log.info("Suppression de l'utilisateur avec l'ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Utilisateur avec l'ID " + id + " non trouvé");
        }
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getUserTickets(Long userId) {
        log.debug("Récupération des tickets de l'utilisateur: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Utilisateur avec l'ID " + userId + " non trouvé"));
        
        return user.getTickets().stream()
            .map(ticketMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username) {
        log.debug("Recherche de l'utilisateur: {}", username);
        return userRepository.findByUsername(username)
            .map(userMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Utilisateur " + username + " non trouvé"));
    }
}