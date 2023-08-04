package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Characteristic;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public enum ProductCharacteristicDto {;

    public record Create(
            @Positive
            Long characteristicId,
            @NotNull
            Characteristic.Type type,
            Set<String> selectedOptions,
            String value
    ){}

    public record Default(
            @Positive
            Long id,
            @NotNull
            Characteristic.Type type,
            Set<String> selectedOptions,
            String value,
            @Positive
            Long characteristicId
    ){}
}
