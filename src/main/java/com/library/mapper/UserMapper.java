package com.library.mapper;

import com.library.dto.UserDTO;
import com.library.entity.User;
import org.springframework.stereotype.Component;

/**
 * UserMapper - Entity <-> DTO conversion (never map password to DTO)
 */
@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}
