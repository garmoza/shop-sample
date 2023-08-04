package dev.garmoza.shopsample.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super(String.format("Category with %d id not found", id));
    }
}
