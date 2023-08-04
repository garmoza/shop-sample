package dev.garmoza.shopsample.exception;

public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(Long id) {
        super(String.format("Organization with %d id not found", id));
    }
}
