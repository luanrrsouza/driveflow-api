package com.dev.driveflowapi.application.dto.output.dealer;

import java.util.UUID;

public record DealerOutput(
        UUID id,
        String corporateName,
        String cnpj,
        String zipCode,
        String address,
        String number
) {
}