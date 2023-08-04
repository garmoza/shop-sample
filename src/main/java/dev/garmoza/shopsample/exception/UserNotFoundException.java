package dev.garmoza.shopsample.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(String.format("User with %d id not found", id));
    }
}
