package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.garmoza.shopsample.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("username", "test@mail.com", "pass");
        user.setAuthorities(Set.of("ROLE_CONSUMER"));
    }

    @Test
    void save() {
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isPositive();
    }

    @Test
    void save_generatesId() {
        User user2 = new User("username2", "test2@email.com", "pass2");
        user2.setAuthorities(Set.of("ROLE_CUSTOMER"));

        userRepository.save(user);
        userRepository.save(user2);

        assertEquals(user.getId() + 1, user2.getId());
    }

    @Test
    void findAll() {
        User user2 = new User("username2", "test2@email.com", "pass2");
        user2.setAuthorities(Set.of("ROLE_CUSTOMER"));

        userRepository.save(user);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        assertThat(userList)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    void findById() {
        userRepository.save(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());

        assertThat(optionalSavedUser).isPresent();
    }

    @Test
    void findById_userNotExist() {
        userRepository.save(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId() + 1);

        assertThat(optionalSavedUser).isNotPresent();
    }

    @Test
    void save_update() {
        userRepository.save(user);

        Optional<User> optionalSavedUser = userRepository.findById(user.getId());
        assertThat(optionalSavedUser).isPresent();
        User savedUser = optionalSavedUser.get();
        savedUser.setUsername("new-username");
        savedUser.setEmail("new@mail.com");
        savedUser.setPassword("new-pass");
        savedUser.setBalance(new BigDecimal("5.55"));
        savedUser.setStatus(User.Status.FROZEN);
        savedUser.setAuthorities(new HashSet<>(Set.of("ROLE_ADMIN")));

        User updatedUser = userRepository.save(savedUser);

        assertThat(updatedUser)
                .usingRecursiveComparison()
                .isEqualTo(savedUser);
    }

    @Test
    void deleteById() {
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> optionalUser = userRepository.findById(user.getId());

        assertThat(optionalUser).isNotPresent();
    }

    @Test
    void deleteById_userNotExist() {
        userRepository.save(user);

        userRepository.deleteById(user.getId() + 1);
        Optional<User> optionalUser = userRepository.findById(user.getId() + 1);

        assertThat(optionalUser).isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_username_notBlank(String username) {
        user.setUsername(username);

        var e = assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
        assertNotBlankAnnotation(e.getConstraintViolations(), "username");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_email_notBlank(String email) {
        user.setEmail(email);

        var e = assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
        assertNotBlankAnnotation(e.getConstraintViolations(), "email");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_password_notBlank(String password) {
        user.setPassword(password);

        var e = assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
        assertNotBlankAnnotation(e.getConstraintViolations(), "password");
    }

    @Test
    void validation_balance_notNull() {
        user.setBalance(null);

        var e = assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
        assertNotNullAnnotation(e.getConstraintViolations(), "balance");
    }

    @Test
    void validation_status_notNull() {
        user.setStatus(null);

        var e = assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
        assertNotNullAnnotation(e.getConstraintViolations(), "status");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_authorities_notEmpty(Set<String> authorities) {
        user.setAuthorities(authorities);

        var e = assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
        assertNotEmptyAnnotation(e.getConstraintViolations(), "authorities");
    }
}