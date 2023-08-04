package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.ProductCharacteristicDto;
import dev.garmoza.shopsample.entity.Characteristic;
import dev.garmoza.shopsample.entity.Product;
import dev.garmoza.shopsample.entity.ProductCharacteristic;

import java.util.List;

public interface ProductCharacteristicService {
    ProductCharacteristic saveNewProductCharacteristic(Product product, Characteristic characteristic, ProductCharacteristic productCharacteristic);
    ProductCharacteristicDto.Default createProductCharacteristic(Long productId, ProductCharacteristicDto.Create dto);
    List<ProductCharacteristicDto.Default> findAllProductCharacteristic(Long productId);
}
