package dev.garmoza.shopsample.service.impl;

import dev.garmoza.shopsample.dto.UserDto;
import dev.garmoza.shopsample.dto.UserDtoMapper;
import dev.garmoza.shopsample.entity.User;
import dev.garmoza.shopsample.exception.UserNotFoundException;
import dev.garmoza.shopsample.repository.UserRepository;
import dev.garmoza.shopsample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper dtoMapper;

    @Override
    public User saveNewUser(User user) {
        user.setAuthorities(Set.of("ROLE_CUSTOMER"));
        return userRepository.save(user);
    }

    @Override
    public UserDto.Default createUser(UserDto.Create userDto) {
        User newUser = dtoMapper.toEntity(userDto);

        User user = saveNewUser(newUser);

        return dtoMapper.toDtoDefault(user);
    }

    @Override
    public List<UserDto.Default> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(dtoMapper::toDtoDefault)
                .toList();
    }

    @Override
    public UserDto.Default findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return dtoMapper.toDtoDefault(user);
    }

    @Override
    public UserDto.Default updateUser(UserDto.Default userDto) {
        User updatedUser = userRepository.findById(userDto.id())
                .map(user -> {
                    user.setUsername(userDto.username());
                    user.setEmail(userDto.email());
                    user.setBalance(userDto.balance());
                    user.setStatus(userDto.status());
                    user.setAuthorities(userDto.authorities());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(userDto.id()));

        return dtoMapper.toDtoDefault(updatedUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
