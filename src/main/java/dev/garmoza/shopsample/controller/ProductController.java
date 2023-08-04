package dev.garmoza.shopsample.controller;

import dev.garmoza.shopsample.dto.ProductDto;
import dev.garmoza.shopsample.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto.Default> getAllProducts() {
        return productService.findAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto.Default postProduct(@Valid @RequestBody ProductDto.Create productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping
    public ProductDto.Default putProduct(@Valid @RequestBody ProductDto.Default productDto) {
        return productService.updateProduct(productDto);
    }

    @GetMapping("/{id}")
    public ProductDto.Full getProductById(@PathVariable @Positive Long id) {
        return productService.findProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable @Positive Long id) {
        productService.deleteProductById(id);
    }
}
