package dev.garmoza.shopsample.controller;

import dev.garmoza.shopsample.dto.CategoryDto;
import dev.garmoza.shopsample.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto.Full> getAllCategories() {
        return categoryService.findAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto.Default postCategory(@Valid @RequestBody CategoryDto.Create categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto.Default postCategoryByParentId(@PathVariable @Positive Long id, @Valid @RequestBody CategoryDto.Create categoryDto) {
        return categoryService.createCategory(id, categoryDto);
    }
}
