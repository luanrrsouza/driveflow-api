package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.port.out.ZipCodeGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock private ZipCodeGateway zipCodeGateway;
    @InjectMocks private AddressService addressService;

    @Test
    void delegatesZipCodeLookupToGateway() {
        AddressOutput expected = new AddressOutput("01001-000", "Rua Central, Sao Paulo - SP");
        when(zipCodeGateway.findByZipCode("01001000")).thenReturn(expected);

        assertThat(addressService.findByZipCode("01001000")).isEqualTo(expected);

        verify(zipCodeGateway).findByZipCode("01001000");
    }
}
