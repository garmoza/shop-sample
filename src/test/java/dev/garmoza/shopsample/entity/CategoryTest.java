package dev.garmoza.shopsample.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("category name");
    }

    @Test
    void createCategory() {
        Category category = new Category("category name");

        assertNull(category.getId());
        assertEquals("category name", category.getName());
        assertNull(category.getParent());
        assertNull(category.getChildren());
    }

    @Test
    void setId() {
        category.setId(2L);

        assertEquals(2L, category.getId());
    }

    @Test
    void setName() {
        category.setName("new category name");

        assertEquals("new category name", category.getName());
    }

    @Test
    void setParent() {
        Category parent = new Category("parent category");
        category.setParent(parent);

        assertEquals(parent, category.getParent());
    }

    @Test
    void setChildren() {
        Category child1 = new Category("child category 1");
        Category child2 = new Category("child category 2");
        List<Category> children = new ArrayList<>(List.of(child1, child2));

        category.setChildren(children);
        assertEquals(children, category.getChildren());
    }
}