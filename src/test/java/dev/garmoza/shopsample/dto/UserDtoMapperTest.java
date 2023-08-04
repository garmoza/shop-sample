package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {

    private UserDtoMapper userDtoMapper;

    @BeforeEach
    void setUp() {
        userDtoMapper = new UserDtoMapper();
    }

    @Test
    void toEntity() {
        UserDto.Create userDtoCreate = new UserDto.Create("username", "test@mail.com", "pass");
        User user = new User("username", "test@mail.com", "pass");

        User userEntity = userDtoMapper.toEntity(userDtoCreate);

        assertThat(userEntity)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void toDtoDefault() {
        // given
        User user = new User("username", "test@mail.com", "pass");
        user.setId(2L);
        user.setBalance(BigDecimal.ZERO);
        user.setStatus(User.Status.ENABLED);
        user.setAuthorities(Set.of("ROLE_CUSTOMER"));

        // when
        UserDto.Default dto = userDtoMapper.toDtoDefault(user);

        // then
        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(new UserDto.Default(
                        2L,
                        "username",
                        "test@mail.com",
                        BigDecimal.ZERO,
                        User.Status.ENABLED,
                        Set.of("ROLE_CUSTOMER")
                ));
    }
}