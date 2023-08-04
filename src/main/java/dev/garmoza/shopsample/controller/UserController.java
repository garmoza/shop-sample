package dev.garmoza.shopsample.controller;

import dev.garmoza.shopsample.dto.UserDto;
import dev.garmoza.shopsample.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto.Default> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.Default postUser(@Valid @RequestBody UserDto.Create userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping
    public UserDto.Default putUser(@Valid @RequestBody UserDto.Default userDto) {
        return userService.updateUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto.Default getUserById(@PathVariable @Positive Long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable @Positive Long id) {
        userService.deleteUserById(id);
    }
}
