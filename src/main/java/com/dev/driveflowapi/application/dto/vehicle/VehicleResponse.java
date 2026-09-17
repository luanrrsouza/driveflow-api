package com.dev.driveflowapi.application.dto.vehicle;

import com.dev.driveflowapi.domain.model.FuelType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record VehicleResponse(

        UUID id,

        String brand,

        String model,

        Set<FuelType> fuelTypes,

        String color,

        Integer year,

        BigDecimal price,

        UUID dealerId,

        String dealerName

) {
}