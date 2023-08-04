package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductCharacteristicDtoMapper {

    public ProductCharacteristicDto.Default toDtoDefault(ProductCharacteristic entity) {
        Set<String> selectedOptions = getProductCharacteristicSelectedOptions(entity);
        String value = getProductCharacteristicValue(entity);

        return new ProductCharacteristicDto.Default(
                entity.getId(),
                entity.getType(),
                selectedOptions,
                value,
                entity.getCharacteristic().getId()
        );
    }

    private Set<String> getProductCharacteristicSelectedOptions(ProductCharacteristic entity) {
        return switch (entity.getType()) {
            case SELECT -> ((ProductCharacteristicSelect) entity).getSelectedOptions();
            case TEXT -> Set.of();
        };
    }

    private String getProductCharacteristicValue(ProductCharacteristic entity) {
        return switch (entity.getType()) {
            case SELECT -> "";
            case TEXT -> ((ProductCharacteristicText) entity).getValue();
        };
    }
}
