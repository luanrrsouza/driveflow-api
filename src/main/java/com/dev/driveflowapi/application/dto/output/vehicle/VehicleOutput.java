package com.dev.driveflowapi.application.dto.output.vehicle;

import com.dev.driveflowapi.domain.model.FuelType;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.Set;

public record VehicleOutput(

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
