package com.dev.driveflowapi.application.dto.input.dealer;

public record UpdateDealerInput(
        String corporateName,
        String cnpj,
        String zipCode
) {
}
