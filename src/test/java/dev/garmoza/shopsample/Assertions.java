package dev.garmoza.shopsample;

import jakarta.validation.ConstraintViolation;
import org.springframework.test.context.transaction.TestTransaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class Assertions {

    private static final String NOT_NULL = "{jakarta.validation.constraints.NotNull.message}";
    private static final String NOT_BLANK = "{jakarta.validation.constraints.NotBlank.message}";
    private static final String NOT_EMPTY = "{jakarta.validation.constraints.NotEmpty.message}";
    private static final String POSITIVE = "{jakarta.validation.constraints.Positive.message}";
    private static final String MIN = "{jakarta.validation.constraints.Min.message}";

    public static void assertNotNullAnnotation(Set<ConstraintViolation<?>> violations, String fieldName) {
        assertThat(violations
                .stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName) && v.getMessageTemplate().equals(NOT_NULL))
                .findAny()
        )
                .withFailMessage("No '@NotNull' annotation for '%s' field", fieldName)
                .isPresent();
    }

    public static void assertNotBlankAnnotation(Set<ConstraintViolation<?>> violations, String fieldName) {
        assertThat(violations
                .stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName) && v.getMessageTemplate().equals(NOT_BLANK))
                .findAny()
        )
                .withFailMessage("No '@NotBlank' annotation for '%s' field", fieldName)
                .isPresent();
    }

    public static void assertNotEmptyAnnotation(Set<ConstraintViolation<?>> violations, String fieldName) {
        assertThat(violations
                .stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName) && v.getMessageTemplate().equals(NOT_EMPTY))
                .findAny()
        )
                .withFailMessage("No '@NotEmpty' annotation for '%s' field", fieldName)
                .isPresent();
    }

    public static void assertPositiveAnnotation(Set<ConstraintViolation<?>> violations, String fieldName) {
        assertThat(violations
                .stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName) && v.getMessageTemplate().equals(POSITIVE))
                .findAny()
        )
                .withFailMessage("No '@Positive' annotation for '%s' field", fieldName)
                .isPresent();
    }

    public static void assertMinAnnotation(Set<ConstraintViolation<?>> violations, String fieldName) {
        assertThat(violations
                .stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName) && v.getMessageTemplate().equals(MIN))
                .findAny()
        )
                .withFailMessage("No '@Min()' annotation for '%s' field", fieldName)
                .isPresent();
    }

    public static void assertValidationConstraints(Class<?> entityClass, Class<? extends Record> dtoClass) {
        assertValidationConstraints(entityClass, dtoClass, Set.of());
    }

    public static void assertValidationConstraints(Class<?> entityClass, Class<? extends Record> dtoClass, Set<String> testedDtoFields) {
        Map<String, Field> entityFields = new HashMap<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            entityFields.put(field.getName(), field);
        }

        Field[] dtoFields = dtoClass.getDeclaredFields();
        for (Field dtoField : dtoFields) {
            // check filed by name and type
            boolean dtoFieldInEntity = entityFields.containsKey(dtoField.getName()) &&
                    entityFields.get(dtoField.getName()).getType().equals(dtoField.getType());

            if (!dtoFieldInEntity) {
                // fail tests if the field has not been previously tested
                assertTrue(
                        testedDtoFields.contains(dtoField.getName()),
                        String.format("DTO field '%s' has not been tested: %s", dtoField.getName(), dtoClass)
                );
                return;
            }

            // get annotations for current field
            Field entityField = entityFields.get(dtoField.getName());
            List<Annotation> entityAnnotations = Arrays.stream(entityField.getAnnotations())
                    .filter(a -> a.annotationType().getPackage().getName().equals("jakarta.validation.constraints"))
                    .toList();
            Set<Annotation> dtoAnnotations = Arrays.stream(dtoField.getAnnotations())
                    .filter(a -> a.annotationType().getPackage().getName().equals("jakarta.validation.constraints"))
                    .collect(Collectors.toSet());

            for (Annotation entityAnnotation : entityAnnotations) {
                assertTrue(
                        dtoAnnotations.contains(entityAnnotation),
                        String.format("Annotation from Entity is not contained in DTO '%s' field: %s", dtoField.getName(), dtoClass)
                );
            }
        }
    }

    public static void commit(Runnable operation) {
        if (TestTransaction.isActive()) {
            TestTransaction.end();
        }
        TestTransaction.start();
        TestTransaction.flagForCommit();

        operation.run();

        TestTransaction.end();
    }
}
