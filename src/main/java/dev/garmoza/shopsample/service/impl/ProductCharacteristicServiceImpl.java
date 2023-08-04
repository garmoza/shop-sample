package dev.garmoza.shopsample.service.impl;

import dev.garmoza.shopsample.dto.ProductCharacteristicDto;
import dev.garmoza.shopsample.dto.ProductCharacteristicDtoMapper;
import dev.garmoza.shopsample.entity.*;
import dev.garmoza.shopsample.exception.CharacteristicNotFoundException;
import dev.garmoza.shopsample.exception.ProductNotFoundException;
import dev.garmoza.shopsample.repository.CharacteristicRepository;
import dev.garmoza.shopsample.repository.ProductCharacteristicRepository;
import dev.garmoza.shopsample.repository.ProductRepository;
import dev.garmoza.shopsample.service.ProductCharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductCharacteristicServiceImpl implements ProductCharacteristicService {

    private final ProductCharacteristicRepository productCharacteristicRepository;
    private final ProductRepository productRepository;
    private final CharacteristicRepository characteristicRepository;
    private final ProductCharacteristicDtoMapper dtoMapper;

    @Override
    public ProductCharacteristic saveNewProductCharacteristic(Product product, Characteristic characteristic, ProductCharacteristic productCharacteristic) {
        if (!product.getCategory().equals(characteristic.getCategory())) {
            throw new RuntimeException("Product and characteristic category does not match");
        }

        if (characteristic.getType() != productCharacteristic.getType()) {
            throw new RuntimeException("Characteristic type does not match");
        }

        if (characteristic.getType() == Characteristic.Type.SELECT) {
            CharacteristicSelect characteristicSelect = (CharacteristicSelect) characteristic;
            ProductCharacteristicSelect productCharacteristicSelect = (ProductCharacteristicSelect) productCharacteristic;

            Set<String> options = new HashSet<>(characteristicSelect.getOptions());
            for (String selectedOption : productCharacteristicSelect.getSelectedOptions()) {
                if (!options.contains(selectedOption)) {
                    throw new RuntimeException("Selected option not exist");
                }
            }
        }

        productCharacteristic.setProduct(product);
        productCharacteristic.setCharacteristic(characteristic);
        return productCharacteristicRepository.save(productCharacteristic);
    }

    @Override
    public ProductCharacteristicDto.Default createProductCharacteristic(Long productId, ProductCharacteristicDto.Create dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        Characteristic characteristic = characteristicRepository.findById(dto.characteristicId())
                .orElseThrow(() -> new CharacteristicNotFoundException(dto.characteristicId()));

        ProductCharacteristic newProductCharacteristic = switch (dto.type()) {
            case SELECT -> new ProductCharacteristicSelect(dto.selectedOptions());
            case TEXT -> new ProductCharacteristicText(dto.value());
        };

        ProductCharacteristic productCharacteristic = saveNewProductCharacteristic(product, characteristic, newProductCharacteristic);

        return dtoMapper.toDtoDefault(productCharacteristic);
    }

    @Override
    public List<ProductCharacteristicDto.Default> findAllProductCharacteristic(Long productId) {
        return productCharacteristicRepository.findAllByProductId(productId)
                .stream()
                .map(dtoMapper::toDtoDefault)
                .toList();
    }
}
