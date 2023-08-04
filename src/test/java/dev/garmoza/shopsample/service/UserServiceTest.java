package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.UserDto;
import dev.garmoza.shopsample.dto.UserDtoMapper;
import dev.garmoza.shopsample.entity.User;
import dev.garmoza.shopsample.exception.UserNotFoundException;
import dev.garmoza.shopsample.repository.UserRepository;
import dev.garmoza.shopsample.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user;
    private UserDto.Create userDtoCreate;
    private UserDto.Default userDtoDefault;

    @BeforeEach
    void setUp() {
        user = new User("username", "test@mail.com", "pass");
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
    void saveNewUser_savesAndReturnsSavedUser() {
        given(userRepository.save(Mockito.any(User.class))).will(i -> i.getArgument(0));

        User savedUser = userService.saveNewUser(user);

        then(userRepository).should().save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        assertThat(value).isEqualTo(savedUser);
    }

    @Test
    void saveNewUser_addsRoleCustomer() {
        given(userRepository.save(Mockito.any(User.class))).willAnswer(i -> i.getArgument(0));

        User savedUser = userService.saveNewUser(user);

        then(userRepository).should().save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        assertThat(value.getAuthorities()).contains("ROLE_CUSTOMER");
        assertThat(value).isEqualTo(savedUser);
    }

    @Test
    void createUser_savesUserAndReturnsUserAsUserDtoDefault() {
        given(userDtoMapper.toEntity(userDtoCreate)).willReturn(user);
        given(userRepository.save(user)).willReturn(user);
        given(userDtoMapper.toDtoDefault(user)).willReturn(userDtoDefault);

        UserDto.Default savedUserDtoDefault = userService.createUser(userDtoCreate);

        then(userRepository).should().save(user);
        assertThat(savedUserDtoDefault).isEqualTo(userDtoDefault);
    }

    @Test
    void findAllUsers_returnsListOfUserDtoDefault() {
        List<User> userList = List.of(user, user, user);
        given(userRepository.findAll()).willReturn(userList);
        given(userDtoMapper.toDtoDefault(Mockito.any(User.class))).willReturn(userDtoDefault);

        List<UserDto.Default> userDtoList = userService.findAllUsers();

        InOrder inOrder = Mockito.inOrder(userRepository, userDtoMapper);
        then(userRepository).should(inOrder).findAll();
        then(userDtoMapper).should(inOrder, times(3)).toDtoDefault(user);
        inOrder.verifyNoMoreInteractions();

        assertThat(userDtoList).hasSize(3);
    }

    @Test
    void findUserById_returnsUserDtoDefault() {
        user.setId(1L);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userDtoMapper.toDtoDefault(user)).willReturn(userDtoDefault);

        UserDto.Default returnedDto = userService.findUserById(user.getId());

        InOrder inOrder = Mockito.inOrder(userRepository, userDtoMapper);
        then(userRepository).should(inOrder).findById(user.getId());
        then(userDtoMapper).should(inOrder).toDtoDefault(user);
        inOrder.verifyNoMoreInteractions();

        assertThat(returnedDto).isEqualTo(userDtoDefault);
    }

    @Test
    void findUserById_userNotFound() {
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    void updateUser_returnsUpdatedUserAsUserDtoDefault() {
        UserDto.Default userDtoDefaultForUpdate = new UserDto.Default(
                user.getId(),
                "new-username",
                "another@mail.com",
                new BigDecimal("4.75"),
                User.Status.ENABLED,
                Set.of("ROLE_ADMIN")
        );

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);
        given(userDtoMapper.toDtoDefault(user)).willReturn(userDtoDefault);

        UserDto.Default updatedUserDto = userService.updateUser(userDtoDefaultForUpdate);

        // updated instance before save
        then(userRepository).should().save(userArgumentCaptor.capture());
        User userBeforeSave = userArgumentCaptor.getValue();
        assertEquals(userDtoDefaultForUpdate.id(), userBeforeSave.getId());
        assertEquals(userDtoDefaultForUpdate.username(), userBeforeSave.getUsername());
        assertEquals(userDtoDefaultForUpdate.balance(), userBeforeSave.getBalance());
        assertEquals(userDtoDefaultForUpdate.status(), userBeforeSave.getStatus());
        assertEquals(userDtoDefaultForUpdate.authorities(), userBeforeSave.getAuthorities());

        assertThat(updatedUserDto).isEqualTo(userDtoDefault);
    }

    @Test
    void updateUser_userNotFound() {
        given(userRepository.findById(userDtoDefault.id())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDtoDefault));
    }

    @Test
    void deleteUserById_returnsVoid() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUserById(userId);

        then(userRepository).should().deleteById(userId);
    }
}