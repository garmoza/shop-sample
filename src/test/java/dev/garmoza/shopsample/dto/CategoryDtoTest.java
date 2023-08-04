package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Category;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static dev.garmoza.shopsample.Assertions.assertPositiveAnnotation;
import static dev.garmoza.shopsample.Assertions.assertValidationConstraints;

class CategoryDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validation_create() {
        assertValidationConstraints(Category.class, CategoryDto.Create.class);
    }

    @Test
    void validation_default() {
        assertValidationConstraints(Category.class, CategoryDto.Default.class);
    }

    @Test
    void validation_default_id_positive() {
        CategoryDto.Default dto = new CategoryDto.Default(
                -2L,
                "name"
        );

        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        assertPositiveAnnotation(violations, "id");
    }

    @Test
    void validation_full() {
        assertValidationConstraints(Category.class, CategoryDto.Full.class);
    }

    @Test
    void validation_full_id_positive() {
        CategoryDto.Full dto = new CategoryDto.Full(
                -2L,
                "name",
                null
        );

        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        assertPositiveAnnotation(violations, "id");
    }
}