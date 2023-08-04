package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDtoMapper {

    public Product toEntity(ProductDto.Create productDto) {
        return new Product(
                productDto.name(),
                productDto.description(),
                productDto.price(),
                productDto.stock()
        );
    }

    public ProductDto.Default toDtoDefault(Product product) {
        return new ProductDto.Default(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreationTime()
        );
    }

    public ProductDto.Full toDtoFull(Product product) {
        return new ProductDto.Full(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getStatus(),
                product.getCreationTime(),
                product.getOrganization().getId(),
                product.getCategory().getId()
        );
    }
}
