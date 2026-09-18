package com.dev.driveflowapi.application.dto.input.vehicle;

import com.dev.driveflowapi.domain.model.FuelType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record CreateVehicleInput(

        String brand,
        String model,
        Set<FuelType> fuelTypes,
        String color,
        Integer year,
        BigDecimal price,
        UUID dealerId
) {
}
