package com.dev.driveflowapi.domain.model;

import com.dev.driveflowapi.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle {

    private UUID id;

    private String brand;

    private String model;

    private Set<FuelType> fuelTypes;

    private String color;

    private Integer year;

    private BigDecimal price;

    private Dealer dealer;

    public Vehicle(
            UUID id,
            String brand,
            String model,
            Set<FuelType> fuelTypes,
            String color,
            Integer year,
            BigDecimal price,
            Dealer dealer
    ) {

        validateBrand(brand);
        validateModel(model);
        validateFuelTypes(fuelTypes);
        validateColor(color);
        validateYear(year);
        validatePrice(price);

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.fuelTypes = fuelTypes;
        this.color = color;
        this.year = year;
        this.price = price;
        this.dealer = dealer;
    }

    private void validateBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new DomainException("Brand cannot be empty.");
        }
    }

    private void validateModel(String model) {
        if (model == null || model.isBlank()) {
            throw new DomainException("Model cannot be empty.");
        }
    }

    private void validateFuelTypes(Set<FuelType> fuelTypes) {
        if (fuelTypes == null || fuelTypes.isEmpty()) {
            throw new DomainException(
                    "At least one fuel type must be informed."
            );
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isBlank()) {
            throw new DomainException("Color cannot be empty.");
        }
    }

    private void validateYear(Integer year) {

        if (year == null) {
            return;
        }

        int currentYear = Year.now().getValue();

        if (year < 1886 || year > currentYear + 1) {
            throw new DomainException("Invalid vehicle year.");
        }
    }

    private void validatePrice(BigDecimal price) {

        if (price == null) {
            return;
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Price cannot be negative.");
        }
    }

    public void update(
            String brand,
            String model,
            Set<FuelType> fuelTypes,
            String color,
            Integer year,
            BigDecimal price
    ) {
        validateBrand(brand);
        validateModel(model);
        validateFuelTypes(fuelTypes);
        validateColor(color);
        validateYear(year);
        validatePrice(price);

        this.brand = brand;
        this.model = model;
        this.fuelTypes = fuelTypes;
        this.color = color;
        this.year = year;
        this.price = price;
    }

    public void assignDealer(Dealer dealer) {
        if (dealer == null) {
            throw new DomainException("Dealer cannot be null.");
        }

        this.dealer = dealer;
    }

}