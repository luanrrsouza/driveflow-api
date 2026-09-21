package com.dev.driveflowapi.infrastructure.integration.viacep;

import com.dev.driveflowapi.domain.exception.ZipCodeNotFoundException;
import com.dev.driveflowapi.infrastructure.integration.viacep.dto.ViaCepResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViaCepAdapterTest {

    @Mock private ViaCepClient viaCepClient;
    @InjectMocks private ViaCepAdapter viaCepAdapter;

    @Test
    void mapsViaCepResponseToApplicationAddress() {
        ViaCepResponse response = new ViaCepResponse("01001-000", "Praca da Se", "Se", "Sao Paulo", "SP", false);
        when(viaCepClient.findByZipCode("01001000")).thenReturn(response);

        var address = viaCepAdapter.findByZipCode("01001000");

        assertThat(address.zipCode()).isEqualTo("01001-000");
        assertThat(address.address()).isEqualTo("Praca da Se, Se, Sao Paulo - SP");
        verify(viaCepClient).findByZipCode("01001000");
    }

    @Test
    void throwsDomainExceptionWhenViaCepDoesNotFindZipCode() {
        when(viaCepClient.findByZipCode("00000000"))
                .thenReturn(new ViaCepResponse(null, null, null, null, null, true));

        assertThatThrownBy(() -> viaCepAdapter.findByZipCode("00000000"))
                .isInstanceOf(ZipCodeNotFoundException.class)
                .hasMessageContaining("00000000");
    }
}
