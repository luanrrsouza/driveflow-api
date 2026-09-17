package com.dev.driveflowapi.application.dto.dealer;

public record CreateDealerRequest(

        String corporateName,

        String cnpj,

        String zipCode,

        String address

) {
}