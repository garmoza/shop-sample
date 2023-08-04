package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Characteristic;
import dev.garmoza.shopsample.entity.CharacteristicSelect;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharacteristicDtoMapper {

    public CharacteristicDto.Default toDtoDefault(Characteristic characteristic) {
        return new CharacteristicDto.Default(
                characteristic.getId(),
                characteristic.getName(),
                characteristic.getType(),
                getCharacteristicOptions(characteristic)
        );
    }

    public CharacteristicDto.Full toDtoFull(Characteristic characteristic) {
        Category category = characteristic.getCategory();

        return new CharacteristicDto.Full(
                characteristic.getId(),
                characteristic.getName(),
                characteristic.getType(),
                getCharacteristicOptions(characteristic),
                category.getId(),
                category.getName()
        );
    }

    private List<String> getCharacteristicOptions(Characteristic characteristic) {
        return switch (characteristic.getType()) {
            case SELECT -> ((CharacteristicSelect) characteristic).getOptions();
            case TEXT -> List.of();
        };
    }
}
