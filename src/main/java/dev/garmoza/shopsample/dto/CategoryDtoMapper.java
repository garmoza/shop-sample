package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDtoMapper {

    public CategoryDto.Default toDtoDefault(Category category) {
        return new CategoryDto.Default(category.getId(), category.getName());
    }

    public CategoryDto.Full toDtoFull(Category category) {
        List<Category> children = category.getChildren() == null ? List.of() : category.getChildren();

        return new CategoryDto.Full(
                category.getId(),
                category.getName(),
                children.stream()
                        .map(this::toDtoFull)
                        .toList()
        );
    }
}
