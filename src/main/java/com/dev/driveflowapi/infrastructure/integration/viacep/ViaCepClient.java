package com.dev.driveflowapi.infrastructure.integration.viacep;

import com.dev.driveflowapi.infrastructure.integration.viacep.dto.ViaCepResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient {

    private final RestClient restClient;

    public ViaCepClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://viacep.com.br")
                .build();
    }

    public ViaCepResponse findByZipCode(String zipCode) {
        return restClient
                .get()
                .uri("/ws/{zipCode}/json/", zipCode)
                .retrieve()
                .body(ViaCepResponse.class);
    }
}