package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.CharacteristicDto;
import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Characteristic;

import java.util.List;

public interface CharacteristicService {
    Characteristic saveNewCharacteristic(Category category, Characteristic characteristic);
    CharacteristicDto.Default createCharacteristic(CharacteristicDto.Create charDto);
    List<CharacteristicDto.Full> findAllCharacteristics();
}
