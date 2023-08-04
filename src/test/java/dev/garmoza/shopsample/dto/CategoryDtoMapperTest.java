package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryDtoMapperTest {

    private CategoryDtoMapper categoryDtoMapper;

    @BeforeEach
    void setUp() {
        categoryDtoMapper = new CategoryDtoMapper();
    }

    @Test
    void toDtoDefault() {
        Category category = new Category("category name");
        category.setId(1L);

        CategoryDto.Default expected = new CategoryDto.Default(
                category.getId(),
                "category name"
        );

        CategoryDto.Default dto = categoryDtoMapper.toDtoDefault(category);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void toDtoFull() {
        Category category = new Category("category");
        Category subCategory1 = new Category("subcategory1");
        Category subCategory2 = new Category("subcategory2");
        Category subCategory1Sub = new Category("subcategory1 sub");

        category.setId(1L);
        subCategory1.setId(2L);
        subCategory2.setId(3L);
        subCategory1Sub.setId(4L);

        category.setChildren(new ArrayList<>(List.of(subCategory1, subCategory2)));
        subCategory1.setParent(category);
        subCategory2.setParent(category);

        subCategory1.setChildren(new ArrayList<>(List.of(subCategory1Sub)));
        subCategory1Sub.setParent(subCategory1);

        var expected = new CategoryDto.Full(1L, "category", List.of(
                new CategoryDto.Full(2L, "subcategory1", List.of(
                        new CategoryDto.Full(4L, "subcategory1 sub", List.of())
                )),
                new CategoryDto.Full(3L, "subcategory2", List.of())
        ));

        CategoryDto.Full dto = categoryDtoMapper.toDtoFull(category);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}