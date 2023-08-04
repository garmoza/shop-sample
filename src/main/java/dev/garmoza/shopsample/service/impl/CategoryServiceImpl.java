package dev.garmoza.shopsample.service.impl;

import dev.garmoza.shopsample.dto.CategoryDto;
import dev.garmoza.shopsample.dto.CategoryDtoMapper;
import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.exception.CategoryNotFoundException;
import dev.garmoza.shopsample.repository.CategoryRepository;
import dev.garmoza.shopsample.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper dtoMapper;

    @Override
    public Category saveNewCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category saveNewCategory(Category parentCategory, Category category) {
        category.setParent(parentCategory);
        return categoryRepository.save(category);
    }

    @Override
    public CategoryDto.Default createCategory(CategoryDto.Create categoryDto) {
        Category newCategory = new Category(categoryDto.name());

        Category category = saveNewCategory(newCategory);

        return dtoMapper.toDtoDefault(category);
    }

    @Override
    public CategoryDto.Default createCategory(Long parentCategoryId, CategoryDto.Create categoryDto) {
        Category parent = categoryRepository.findById(parentCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(parentCategoryId));
        Category newCategory = new Category(categoryDto.name());

        Category category = saveNewCategory(parent, newCategory);

        return dtoMapper.toDtoDefault(category);
    }

    @Override
    public List<CategoryDto.Full> findAllCategories() {
        return categoryRepository.findAllByParentIsNull()
                .stream()
                .map(dtoMapper::toDtoFull)
                .toList();
    }
}
