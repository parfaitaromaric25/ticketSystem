package com.example.ticket.service;

import com.example.ticket.dto.UserDTO;
import com.example.ticket.dto.mapper.UserMapper;
import com.example.ticket.exception.ResourceNotFoundException;
import com.example.ticket.model.entity.User;
import com.example.ticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private User user;
    private UserDTO userDTO;
    
    @BeforeEach
    void setUp() {
        user = User.builder()
            //.id(1L)
            .username("testuser")
            .email("test@example.com")
            .build();
        
        userDTO = UserDTO.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .build();
    }
    
    @Test
    void testCreateUser() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        
        UserDTO result = userService.create(userDTO);
        
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testFindById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        
        Optional<UserDTO> result = userService.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }
    
    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<UserDTO> result = userService.findById(999L);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    void testFindAll() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        
        List<UserDTO> result = userService.findAll();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testUpdate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        
        UserDTO result = userService.update(1L, userDTO);
        
        assertNotNull(result);
        verify(userMapper, times(1)).updateEntityFromDTO(any(), any());
    }
    
    @Test
    void testUpdate_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.update(999L, userDTO);
        });
    }
    
    @Test
    void testDelete_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        
        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDelete_NotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.delete(999L);
        });
    }
}