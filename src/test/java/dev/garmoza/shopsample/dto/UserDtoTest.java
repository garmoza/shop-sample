package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.garmoza.shopsample.Assertions.*;

class UserDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validation_create() {
        assertValidationConstraints(User.class, UserDto.Create.class);
    }

    @Test
    void validation_create_id_positive() {
        // given
        UserDto.Default dto = new UserDto.Default(
                -2L,
                "username",
                "test@mail.com",
                new BigDecimal("4.75"),
                User.Status.ENABLED,
                Set.of("ROLE_CUSTOMER")
        );

        // when
        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        // then
        assertPositiveAnnotation(violations, "id");
    }

    @Test
    void validation_default() {
        assertValidationConstraints(User.class, UserDto.Default.class);
    }
}