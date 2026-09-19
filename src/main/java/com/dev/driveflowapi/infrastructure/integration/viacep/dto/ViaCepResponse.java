package com.dev.driveflowapi.infrastructure.integration.viacep.dto;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {
}