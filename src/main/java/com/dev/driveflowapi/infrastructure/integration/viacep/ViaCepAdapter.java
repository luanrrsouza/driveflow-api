package com.dev.driveflowapi.infrastructure.integration.viacep;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.port.out.ZipCodeGateway;
import com.dev.driveflowapi.domain.exception.ZipCodeNotFoundException;
import com.dev.driveflowapi.infrastructure.integration.viacep.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViaCepAdapter implements ZipCodeGateway {

    private final ViaCepClient viaCepClient;

    @Override
    public AddressOutput findByZipCode(String zipCode) {

        ViaCepResponse response =
                viaCepClient.findByZipCode(zipCode);

        if (Boolean.TRUE.equals(response.erro())) {
            throw new ZipCodeNotFoundException(zipCode);
        }

        String address = String.format(
                "%s, %s, %s - %s",
                response.logradouro(),
                response.bairro(),
                response.localidade(),
                response.uf()
        );

        return new AddressOutput(
                response.cep(),
                address
        );
    }
}
