package dev.garmoza.shopsample.service.impl;

import dev.garmoza.shopsample.dto.CharacteristicDto;
import dev.garmoza.shopsample.dto.CharacteristicDtoMapper;
import dev.garmoza.shopsample.entity.*;
import dev.garmoza.shopsample.exception.CategoryNotFoundException;
import dev.garmoza.shopsample.repository.CategoryRepository;
import dev.garmoza.shopsample.repository.CharacteristicRepository;
import dev.garmoza.shopsample.service.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacteristicServiceImpl implements CharacteristicService {

    private final CategoryRepository categoryRepository;
    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicDtoMapper dtoMapper;

    public Characteristic saveNewCharacteristic(Category category, Characteristic characteristic) {
        characteristic.setCategory(category);
        return characteristicRepository.save(characteristic);
    }

    public CharacteristicDto.Default createCharacteristic(CharacteristicDto.Create charDto) {
        Category category = categoryRepository.findById(charDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(charDto.categoryId()));

        Characteristic newCharacteristic = switch (charDto.type()) {
            case SELECT -> new CharacteristicSelect(charDto.name(), charDto.options());
            case TEXT -> new CharacteristicText(charDto.name());
        };

        Characteristic characteristic = saveNewCharacteristic(category, newCharacteristic);

        return dtoMapper.toDtoDefault(characteristic);
    }

    public List<CharacteristicDto.Full> findAllCharacteristics() {
        return characteristicRepository.findAll()
                .stream()
                .map(dtoMapper::toDtoFull)
                .toList();
    }
}
