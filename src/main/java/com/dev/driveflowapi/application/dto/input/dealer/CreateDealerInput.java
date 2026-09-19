package com.dev.driveflowapi.application.dto.input.dealer;

public record CreateDealerInput(

        String corporateName,
        String cnpj,
        String zipCode
) {
}
