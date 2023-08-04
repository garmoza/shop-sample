package dev.garmoza.shopsample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.garmoza.shopsample.dto.UserDto;
import dev.garmoza.shopsample.entity.User;
import dev.garmoza.shopsample.exception.UserNotFoundException;
import dev.garmoza.shopsample.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disables security
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto.Create userDtoCreate;
    private UserDto.Default userDtoDefault;

    @BeforeEach
    void setUp() {
        userDtoCreate = new UserDto.Create("username", "test@mail.com", "pass");
        userDtoDefault = new UserDto.Default(
                1L,
                "username",
                "test@mail.com",
                BigDecimal.ZERO,
                User.Status.CONFIRMATION,
                Set.of("ROLE_CUSTOMER")
        );
    }

    @Test
    void getAllUsers_returnsListUserDtoDefault() throws Exception {
        List<UserDto.Default> responseDto = List.of(userDtoDefault);
        given(userService.findAllUsers()).willReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(responseDto.size())));
    }

    @Test
    void postUser_returnsCreated() throws Exception {
        given(userService.createUser(userDtoCreate)).willReturn(userDtoDefault);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDtoCreate))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(print())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "username": "username",
                            "email": "test@mail.com",
                            "balance": 0,
                            "status": "CONFIRMATION",
                            "authorities": ["ROLE_CUSTOMER"]
                        }
                        """
                ));
    }

    @Test
    void postUser_validation() throws Exception {
        UserDto.Create notValid = new UserDto.Create(
                " ",
                " ",
                " "
        );

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notValid))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void putUser_returnsUpdatedUserAsUserDtoDefault() throws Exception {
        UserDto.Default userDtoDefaultForUpdate = new UserDto.Default(
                1L,
                "new-username",
                "another@mail.com",
                new BigDecimal("4.75"),
                User.Status.ENABLED,
                Set.of("ROLE_ADMIN")
        );
        given(userService.updateUser(userDtoDefaultForUpdate)).willReturn(userDtoDefaultForUpdate);

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDtoDefaultForUpdate))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "username": "new-username",
                            "email": "another@mail.com",
                            "balance": 4.75,
                            "status": "ENABLED",
                            "authorities": ["ROLE_ADMIN"]
                        }
                        """
                ));
    }

    @Test
    void putUser_userNotFound() throws Exception {
        given(userService.updateUser(userDtoDefault)).willThrow(new UserNotFoundException(userDtoDefault.id()));

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDtoDefault))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void putUser_validation() throws Exception {
        UserDto.Default userDtoDefaultForUpdate = new UserDto.Default(
                1L,
                " ",
                " ",
                null,
                null,
                null
        );

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDtoDefaultForUpdate))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getUserById_returnsUserDtoDefault() throws Exception {
        Long userId = 1L;
        given(userService.findUserById(userId)).willReturn(userDtoDefault);

        ResultActions response = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "id": 1,
                            "username": "username",
                            "email": "test@mail.com",
                            "balance": 0,
                            "status": "CONFIRMATION",
                            "authorities": ["ROLE_CUSTOMER"]
                        }
                        """
                ));
    }

    @Test
    void getUserById_userNotFound() throws Exception {
        Long userId = 1L;
        given(userService.findUserById(userId)).willThrow(new UserNotFoundException(userId));

        ResultActions response = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -2L})
    void getUserById_validate(long userId) throws Exception {
        ResultActions response = mockMvc.perform(get("/users/%d".formatted(userId))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteUserById() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUserById(userId);

        ResultActions response = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -2L})
    void deleteUserById_validate(long userId) throws Exception {
        ResultActions response = mockMvc.perform(delete("/users/%d".formatted(userId))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}