package dev.garmoza.shopsample.repository;

import dev.garmoza.shopsample.entity.Category;
import jakarta.validation.ConstraintViolationException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.garmoza.shopsample.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("category name");
    }

    @Test
    void save() {
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isPositive();
    }

    @Test
    void save_generatesId() {
        Category category2 = new Category("another category");

        categoryRepository.save(category);
        categoryRepository.save(category2);

        assertEquals(category.getId() + 1, category2.getId());
    }

    @Test
    void findById() {
        categoryRepository.save(category);

        Optional<Category> optionalCategory = categoryRepository.findById(category.getId());

        assertThat(optionalCategory).isPresent();
    }

    @Test
    void findById_categoryNotExist() {
        categoryRepository.save(category);

        Optional<Category> optionalCategory = categoryRepository.findById(category.getId() + 1);

        assertThat(optionalCategory).isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validation_name_notBlank(String name) {
        category.setName(name);

        var e = assertThrows(ConstraintViolationException.class, () -> categoryRepository.save(category));
        assertNotBlankAnnotation(e.getConstraintViolations(), "name");
    }

    @Test
    void findAllByParentIsNull() {
        Category category1 = new Category("category1 without parent");
        Category category2 = new Category("category2 without parent");
        Category subcategory1 = new Category("subcategory1");
        Category subcategory1Sub = new Category("subcategory1 subcategory");
        Category subcategory2 = new Category("subcategory2");

        category1.setChildren(new ArrayList<>(List.of(subcategory1, subcategory2)));
        subcategory1.setParent(category1);
        subcategory2.setParent(category1);

        subcategory2.setChildren(new ArrayList<>(List.of(subcategory1Sub)));
        subcategory1Sub.setParent(subcategory2);

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(subcategory1);
        categoryRepository.save(subcategory2);
        categoryRepository.save(subcategory1Sub);

        List<Category> categories = categoryRepository.findAllByParentIsNull();
        assertThat(categories).hasSize(2);
        assertThat(categories.stream()
                .filter(c -> c.getId().equals(category1.getId()))
                .findFirst()
        ).isPresent();
        assertThat(categories.stream()
                .filter(c -> c.getId().equals(category2.getId()))
                .findFirst()
        ).isPresent();
    }
}