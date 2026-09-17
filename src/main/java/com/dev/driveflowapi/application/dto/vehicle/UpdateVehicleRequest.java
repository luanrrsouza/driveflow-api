package com.dev.driveflowapi.application.dto.vehicle;

import com.dev.driveflowapi.domain.model.FuelType;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateVehicleRequest(

        String brand,

        String model,

        Set<FuelType> fuelTypes,

        String color,

        Integer year,

        BigDecimal price,

        UUID dealerId

) {
}