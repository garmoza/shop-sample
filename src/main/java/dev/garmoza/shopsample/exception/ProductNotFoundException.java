package dev.garmoza.shopsample.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super(String.format("Product with %d id not found", id));
    }
}
