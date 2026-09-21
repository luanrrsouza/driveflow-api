package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {
    @Mock private AddressService addressService;
    @InjectMocks private AddressController controller;

    @Test
    void findsAddressByZipCode() {
        AddressOutput output = new AddressOutput("01001-000", "Rua Central");
        when(addressService.findByZipCode("01001000")).thenReturn(output);

        assertThat(controller.findByZipCode("01001000").getBody()).isEqualTo(output);
    }
}
