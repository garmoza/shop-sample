package dev.garmoza.shopsample.controller;

import dev.garmoza.shopsample.dto.CharacteristicDto;
import dev.garmoza.shopsample.service.CharacteristicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/characteristics")
@RequiredArgsConstructor
public class CharacteristicController {

    private final CharacteristicService characteristicService;

    @GetMapping
    public List<CharacteristicDto.Full> getAllCharacteristics() {
        return characteristicService.findAllCharacteristics();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacteristicDto.Default postCharacteristic(@Valid @RequestBody CharacteristicDto.Create charDto) {
        return characteristicService.createCharacteristic(charDto);
    }
}
