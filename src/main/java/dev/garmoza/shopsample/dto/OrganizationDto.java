package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Organization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public enum OrganizationDto {;

    public record Create(
            @Positive
            Long userId,
            @NotBlank
            String name,
            @NotNull
            String description,
            String logo
    ){}

    public record Default(
            @Positive
            Long id,
            @NotBlank
            String name,
            @NotNull
            String description,
            String logo,
            @NotNull
            BigDecimal balance,
            @NotNull
            Organization.Status status,
            Date creationTime
    ){}
}
