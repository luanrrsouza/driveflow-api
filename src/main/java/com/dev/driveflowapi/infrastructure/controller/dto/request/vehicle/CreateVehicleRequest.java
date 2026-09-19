package com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle;

import com.dev.driveflowapi.domain.model.FuelType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateVehicleRequest(

        @NotBlank(message = "Brand is required.")
        String brand,

        @NotBlank(message = "Model is required.")
        String model,

        @NotEmpty(message = "At least one fuel type is required.")
        Set<FuelType> fuelTypes,

        @NotBlank(message = "Color is required.")
        String color,

        Integer year,

        @PositiveOrZero(message = "Price cannot be negative.")
        BigDecimal price,

        @NotNull(message = "Dealer id is required.")
        UUID dealerId

) {
}