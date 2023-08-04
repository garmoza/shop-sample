package dev.garmoza.shopsample.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public enum CategoryDto {;

    public record Create(
            @NotBlank
            String name
    ){}

    public record Default(
            @Positive
            Long id,
            @NotBlank
            String name
    ){}

    public record Full(
            @Positive
            Long id,
            @NotBlank
            String name,
            List<Full> children
    ){}
}
