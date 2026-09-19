package com.dev.driveflowapi.infrastructure.controller.dto.request.dealer;

import jakarta.validation.constraints.NotBlank;

public record UpdateDealerRequest(

        @NotBlank(message = "Corporate name is required.")
        String corporateName,

        @NotBlank(message = "CNPJ is required.")
        String cnpj,

        @NotBlank(message = "Zip code is required.")
        String zipCode

) {
}