package dev.garmoza.shopsample.controller;

import dev.garmoza.shopsample.dto.ProductCharacteristicDto;
import dev.garmoza.shopsample.service.ProductCharacteristicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
@Validated
public class ProductCharacteristicController {

    private final ProductCharacteristicService productCharacteristicService;

    @GetMapping("/{productId}/characteristics")
    public List<ProductCharacteristicDto.Default> getAllProductCharacteristics(@PathVariable @Positive Long productId) {
        return productCharacteristicService.findAllProductCharacteristic(productId);
    }

    @PostMapping("/{productId}/characteristics")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCharacteristicDto.Default postProductCharacteristic(@PathVariable @Positive Long productId, @Valid ProductCharacteristicDto.Create dto) {
        return productCharacteristicService.createProductCharacteristic(productId, dto);
    }
}
