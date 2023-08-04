package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Characteristic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public enum CharacteristicDto {;

    public record Create(
            @Positive
            Long categoryId,
            @NotBlank
            String name,
            @NotNull
            Characteristic.Type type,
            List<String> options
    ){}

    public record Default(
            @Positive
            Long id,
            @NotBlank
            String name,
            @NotNull
            Characteristic.Type type,
            List<String> options
    ){}

    public record Full(
            Long id,
            String name,
            Characteristic.Type type,
            List<String> options,
            Long categoryId,
            String categoryName
    ){}
}
