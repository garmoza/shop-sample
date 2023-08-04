package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Product;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.garmoza.shopsample.Assertions.assertPositiveAnnotation;
import static dev.garmoza.shopsample.Assertions.assertValidationConstraints;

class ProductDtoTest {

    private Validator validator;

    private Set<String> testedDtoFields;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        testedDtoFields = new HashSet<>();
    }

    @Test
    void validation_create() {
        validation_create_organizationId_positive();
        validation_create_categoryId_positive();

        assertValidationConstraints(Product.class, ProductDto.Create.class, testedDtoFields);
    }

    void validation_create_organizationId_positive() {
        ProductDto.Create dto = new ProductDto.Create(
                -2L,
                1L,
                "name",
                "desc",
                new BigDecimal("4.99"),
                100
        );

        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        assertPositiveAnnotation(violations, "organizationId");

        testedDtoFields.add("organizationId");
    }

    void validation_create_categoryId_positive() {
        ProductDto.Create dto = new ProductDto.Create(
                1L,
                -2L,
                "name",
                "desc",
                new BigDecimal("4.99"),
                100
        );

        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        assertPositiveAnnotation(violations, "categoryId");

        testedDtoFields.add("categoryId");
    }

    @Test
    void validation_default() {
        assertValidationConstraints(Product.class, ProductDto.Default.class);
    }
}