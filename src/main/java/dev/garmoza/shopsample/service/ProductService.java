package dev.garmoza.shopsample.service;

import dev.garmoza.shopsample.dto.ProductDto;
import dev.garmoza.shopsample.entity.Category;
import dev.garmoza.shopsample.entity.Organization;
import dev.garmoza.shopsample.entity.Product;

import java.util.List;

public interface ProductService {
    Product saveNewProduct(Organization org, Category category, Product product);
    ProductDto.Default createProduct(ProductDto.Create productDto);
    List<ProductDto.Default> findAllProducts();
    ProductDto.Full findProductById(Long id);
    ProductDto.Default updateProduct(ProductDto.Default productDto);
    void deleteProductById(Long id);
}
