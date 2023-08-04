package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public User toEntity(UserDto.Create dtoUser) {
        return new User(dtoUser.username(), dtoUser.email(), dtoUser.password());
    }

    public UserDto.Default toDtoDefault(User user) {
        return new UserDto.Default(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBalance(),
                user.getStatus(),
                user.getAuthorities()
        );
    }
}
