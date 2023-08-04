package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.CategoryDto;
import dev.garmoza.shopsample.dto.CategoryDtoMapper;
import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.exception.CategoryNotFoundException;
import dev.garmoza.shopsample.repository.CategoryRepository;
import dev.garmoza.shopsample.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryDtoMapper categoryDtoMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    private Category category;
    private CategoryDto.Create categroyDtoCreate;
    private CategoryDto.Default categoryDtoDefault;
    private CategoryDto.Full categoryDtoFull;

    @BeforeEach
    void setUp() {
        category = new Category("category name");
        categroyDtoCreate = new CategoryDto.Create("category name");
        categoryDtoDefault = new CategoryDto.Default(1L, "category name");
        categoryDtoFull = new CategoryDto.Full(1L, "category name", List.of());
    }

    @Test
    void saveNewCategory() {
        given(categoryRepository.save(Mockito.any(Category.class))).will(i -> i.getArgument(0));

        Category savedCategory = categoryService.saveNewCategory(category);

        then(categoryRepository).should().save(categoryArgumentCaptor.capture());
        Category value = categoryArgumentCaptor.getValue();
        assertThat(value).isEqualTo(savedCategory);
    }

    @Test
    void saveNewCategory_withParentCategory() {
        Category parentCategory = new Category("parentCategory");
        given(categoryRepository.save(Mockito.any(Category.class))).will(i -> i.getArgument(0));

        Category savedCategory = categoryService.saveNewCategory(parentCategory, category);
        then(categoryRepository).should().save(categoryArgumentCaptor.capture());
        Category value = categoryArgumentCaptor.getValue();
        assertThat(value).isEqualTo(savedCategory);
        assertThat(value.getParent()).isEqualTo(parentCategory);
    }

    @Test
    void createCategory() {
        given(categoryRepository.save(Mockito.any(Category.class))).willReturn(category);
        given(categoryDtoMapper.toDtoDefault(category)).willReturn(categoryDtoDefault);

        CategoryDto.Default dto = categoryService.createCategory(categroyDtoCreate);

        then(categoryRepository).should().save(categoryArgumentCaptor.capture());
        Category value = categoryArgumentCaptor.getValue();
        assertEquals("category name", value.getName());
        assertNull(value.getParent());
        assertNull(value.getChildren());

        assertThat(dto).isEqualTo(categoryDtoDefault);
    }

    @Test
    void createCategory_withParentCategoryId() {
        Category parentCategory = new Category("parent category");
        parentCategory.setId(2L);

        given(categoryRepository.findById(parentCategory.getId())).willReturn(Optional.of(parentCategory));
        given(categoryRepository.save(Mockito.any(Category.class))).willReturn(category);
        given(categoryDtoMapper.toDtoDefault(category)).willReturn(categoryDtoDefault);

        CategoryDto.Default dto = categoryService.createCategory(parentCategory.getId(), categroyDtoCreate);

        then(categoryRepository).should().save(categoryArgumentCaptor.capture());
        Category value = categoryArgumentCaptor.getValue();
        assertEquals("category name", value.getName());
        assertThat(value.getParent()).isEqualTo(parentCategory);
        assertNull(value.getChildren());

        assertThat(dto).isEqualTo(categoryDtoDefault);
    }

    @Test
    void createCategory_withParentCategoryId_categoryNotFound() {
        Long parentCategoryId = 2L;
        given(categoryRepository.findById(parentCategoryId)).willReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.createCategory(parentCategoryId, categroyDtoCreate));
    }


    @Test
    void findAllCategories() {
        List<Category> categoryList = List.of(category, category, category);
        given(categoryRepository.findAllByParentIsNull()).willReturn(categoryList);
        given(categoryDtoMapper.toDtoFull(Mockito.any(Category.class))).willReturn(categoryDtoFull);

        List<CategoryDto.Full> dtoList = categoryService.findAllCategories();

        then(categoryRepository).should().findAllByParentIsNull();
        then(categoryDtoMapper).should(times(3)).toDtoFull(category);
        assertThat(dtoList).hasSize(3);
    }
}