package com.example.ticket.dto.mapper;

import com.example.ticket.dto.UserDTO;
import com.example.ticket.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
    
    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        
        return User.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .build();
    }
    
    public void updateEntityFromDTO(UserDTO dto, User user) {
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
    }
}