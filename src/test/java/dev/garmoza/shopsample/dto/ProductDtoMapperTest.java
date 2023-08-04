package dev.garmoza.shopsample.dto;

import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ProductDtoMapperTest {

    private ProductDtoMapper productDtoMapper;

    @BeforeEach
    void setUp() {
        productDtoMapper = new ProductDtoMapper();
    }

    @Test
    void toEntity() {
        ProductDto.Create productDtoCreate = new ProductDto.Create(
                1L,
                2L,
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50
        );
        Product product = new Product(
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50
        );

        Product productEntity = productDtoMapper.toEntity(productDtoCreate);

        assertThat(productEntity)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void toDtoDefault() {
        Product product = new Product(
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50
        );
        product.setId(1L);
        product.setStatus(Product.Status.FROZEN);
        product.setCreationTime(new Date(12345));

        ProductDto.Default dto = productDtoMapper.toDtoDefault(product);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(new ProductDto.Default(
                        1L,
                        "product name",
                        "product desc",
                        new BigDecimal("4.99"),
                        50,
                        Product.Status.FROZEN,
                        new Date(12345)
                ));
    }

    @Test
    void toDtoFull() {
        Product product = new Product(
                "product name",
                "product desc",
                new BigDecimal("4.99"),
                50
        );
        product.setId(1L);
        product.setStatus(Product.Status.FROZEN);
        product.setCreationTime(new Date(12345));

        Organization org = new Organization("org name", "org desc", "logo");
        org.setId(2L);
        product.setOrganization(org);

        Category category = new Category("category name");
        category.setId(3L);
        product.setCategory(category);

        ProductDto.Full dto = productDtoMapper.toDtoFull(product);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(new ProductDto.Full(
                        1L,
                        "product name",
                        "product desc",
                        new BigDecimal("4.99"),
                        50,
                        Product.Status.FROZEN,
                        new Date(12345),
                        2L,
                        3L
                ));
    }
}