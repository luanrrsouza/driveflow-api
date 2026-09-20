package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.port.out.ZipCodeGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final ZipCodeGateway zipCodeGateway;

    public AddressOutput findByZipCode(String zipCode) {
        return zipCodeGateway.findByZipCode(zipCode);
    }
}