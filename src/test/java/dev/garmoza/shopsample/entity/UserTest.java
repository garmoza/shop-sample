package dev.garmoza.shopsample.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User underTest;

    @BeforeEach
    void setUp() {
        underTest = new User("username", "test@mail.com", "pass");
    }

    @Test
    void createUser() {
        // given, when
        User user = new User("username", "test@mail.com", "pass");

        // then
        assertNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(BigDecimal.ZERO, user.getBalance());
        assertEquals(User.Status.CONFIRMATION, user.getStatus());
        assertNull(user.getAuthorities());
    }

    @Test
    void createUserWithBalance() {
        // given, when
        User user = new User("username", "test@mail.com", "pass", new BigDecimal("200.45"));

        // then
        assertNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(new BigDecimal("200.45"), user.getBalance());
        assertEquals(User.Status.CONFIRMATION, user.getStatus());
        assertNull(user.getAuthorities());
    }

    @Test
    void createUserWithBalanceAndStatus() {
        // given, when
        User user = new User("username", "test@mail.com", "pass", new BigDecimal("200.45"), User.Status.ENABLED);

        // then
        assertNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals(new BigDecimal("200.45"), user.getBalance());
        assertEquals(User.Status.ENABLED, user.getStatus());
        assertNull(user.getAuthorities());
    }

    @Test
    void setId() {
        // when
        underTest.setId(2L);

        // then
        assertEquals(2L, underTest.getId());
    }

    @Test
    void setUsername() {
        // when
        underTest.setUsername("another");

        // then
        assertEquals("another", underTest.getUsername());
    }

    @Test
    void setEmail() {
        // when
        underTest.setEmail("another@mail.com");

        // then
        assertEquals("another@mail.com", underTest.getEmail());
    }

    @Test
    void setPassword() {
        // when
        underTest.setPassword("12345");

        // then
        assertEquals("12345", underTest.getPassword());
    }

    @Test
    void setBalance() {
        // when
        BigDecimal balance = new BigDecimal("17.75");
        underTest.setBalance(balance);

        // then
        assertEquals(balance, underTest.getBalance());
    }

    @Test
    void setStatus() {
        // when
        underTest.setStatus(User.Status.FROZEN);

        // then
        assertEquals(User.Status.FROZEN, underTest.getStatus());
    }

    @Test
    void setAuthorities() {
        // when
        underTest.setAuthorities(Set.of("ROLE_ADMIN", "ROLE_CUSTOMER"));

        // then
        assertEquals(Set.of("ROLE_ADMIN", "ROLE_CUSTOMER"), underTest.getAuthorities());
    }
}