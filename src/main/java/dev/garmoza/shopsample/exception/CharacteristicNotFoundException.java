package dev.garmoza.shopsample.exception;

public class CharacteristicNotFoundException extends RuntimeException {

    public CharacteristicNotFoundException(Long id) {
        super(String.format("Characteristic with %d id not found", id));
    }
}
