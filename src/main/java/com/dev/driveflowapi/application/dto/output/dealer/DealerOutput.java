package com.dev.driveflowapi.application.dto.output.dealer;

import java.util.UUID;

public record DealerOutput(
        UUID id,
        String corporareName,
        String cnpj,
        String zipCode,
        String address
) {
}
