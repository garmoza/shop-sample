package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public enum ProductDto {;

    public record Create(
            @Positive
            Long organizationId,
            @Positive
            Long categoryId,
            @NotBlank
            String name,
            @NotNull
            String description,
            @Positive
            BigDecimal price,
            @Min(0)
            long stock
    ){}

    public record Default(
            @Positive
            Long id,
            @NotBlank
            String name,
            @NotNull
            String description,
            @Positive
            BigDecimal price,
            @Min(0)
            long stock,
            @NotNull
            Product.Status status,
            Date creationTime
    ){}

    public record Full(
            Long id,
            String name,
            String description,
            BigDecimal price,
            long stock,
            Product.Status status,
            Date creationTime,
            Long organizationId,
            Long categoryId
    )
    {}
}
