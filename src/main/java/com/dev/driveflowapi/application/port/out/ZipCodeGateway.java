package com.dev.driveflowapi.application.port.out;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;

public interface ZipCodeGateway {

    AddressOutput findByZipCode(String zipCode);
}