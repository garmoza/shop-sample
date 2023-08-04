package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.UserDto;
import dev.garmoza.shopsample.entity.User;

import java.util.List;

public interface UserService {
    User saveNewUser(User user);
    UserDto.Default createUser(UserDto.Create userDto);
    List<UserDto.Default> findAllUsers();
    UserDto.Default findUserById(Long id);
    UserDto.Default updateUser(UserDto.Default userDto);
    void deleteUserById(Long id);
}
