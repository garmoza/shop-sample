package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Organization;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.garmoza.shopsample.Assertions.assertPositiveAnnotation;
import static dev.garmoza.shopsample.Assertions.assertValidationConstraints;

class OrganizationDtoTest {

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
        validation_create_userId_positive();

        assertValidationConstraints(Organization.class, OrganizationDto.Create.class, testedDtoFields);
    }

    void  validation_create_userId_positive() {
        OrganizationDto.Create dto = new OrganizationDto.Create(
                -2L,
                "name",
                "description",
                "logo"
        );

        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        assertPositiveAnnotation(violations, "userId");

        testedDtoFields.add("userId");
    }

    @Test
    void validation_default() {
        assertValidationConstraints(Organization.class, OrganizationDto.Default.class);
    }

    @Test
    void validation_default_id_positive() {
        OrganizationDto.Default dto = new OrganizationDto.Default(
                -2L,
                "name",
                "description",
                "logo",
                BigDecimal.ZERO,
                Organization.Status.ENABLED,
                new Date(12345)
        );

        Set<ConstraintViolation<?>> violations = validator.validate(dto)
                .stream()
                .map(v -> (ConstraintViolation<?>) v)
                .collect(Collectors.toSet());

        assertPositiveAnnotation(violations, "id");
    }
}