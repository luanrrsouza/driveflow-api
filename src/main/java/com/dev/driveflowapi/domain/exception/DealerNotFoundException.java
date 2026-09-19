package com.dev.driveflowapi.domain.exception;

public class DealerNotFoundException extends DomainException {

    public DealerNotFoundException() {
        super("Dealer not found.");
    }
}