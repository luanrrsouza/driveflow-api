package com.dev.driveflowapi.infrastructure.controller.dto.response;

import java.util.UUID;

public record DealerResponse(
        UUID id,
        String corporateName,
        String cnpj,
        String zipCode,
        String address
) {
}