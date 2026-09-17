package com.dev.driveflowapi.application.dto.dealer;

public record UpdateDealerRequest(

        String corporateName,

        String cnpj,

        String zipCode,

        String address

) {
}
