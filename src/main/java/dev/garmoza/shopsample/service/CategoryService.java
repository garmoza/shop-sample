package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.CategoryDto;
import dev.garmoza.shopsample.entity.Category;

import java.util.List;

public interface CategoryService {
    Category saveNewCategory(Category category);
    Category saveNewCategory(Category parentCategory, Category category);
    CategoryDto.Default createCategory(CategoryDto.Create categoryDto);
    CategoryDto.Default createCategory(Long parentCategoryId, CategoryDto.Create categoryDto);
    List<CategoryDto.Full> findAllCategories();
}
