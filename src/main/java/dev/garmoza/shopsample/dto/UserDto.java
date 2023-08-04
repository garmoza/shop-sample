package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public enum UserDto {;

    public record Create(
            @NotBlank
            String username,
            @NotBlank
            String email,
            @NotBlank
            String password
    ){}

    public record Default(
            @Positive
            Long id,
            @NotBlank
            String username,
            @NotBlank
            String email,
            @NotNull
            BigDecimal balance,
            @NotNull
            User.Status status,
            @NotEmpty
            Set<String> authorities
    ){}
}
