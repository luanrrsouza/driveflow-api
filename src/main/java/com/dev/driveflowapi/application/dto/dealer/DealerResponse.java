package com.dev.driveflowapi.application.dto.dealer;

import java.util.UUID;

public record DealerResponse(

        UUID id,

        String corporateName,

        String cnpj,

        String zipCode,

        String address

) {
}