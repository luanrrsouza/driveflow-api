package com.dev.driveflowapi.domain.exception;

public class ZipCodeNotFoundException extends DomainException {

    public ZipCodeNotFoundException(String zipCode) {
        super("Zip code not found: " + zipCode);
    }
}
