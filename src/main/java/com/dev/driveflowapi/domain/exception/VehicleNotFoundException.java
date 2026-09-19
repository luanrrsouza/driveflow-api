package com.dev.driveflowapi.domain.exception;

public class VehicleNotFoundException extends DomainException {

    public VehicleNotFoundException() {
        super("Vehicle not found.");
    }
}
