package com.dev.driveflowapi.domain.model;

import com.dev.driveflowapi.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VehicleTest {

    @Test
    void updatesVehicleAndAssignsDealer() {
        Vehicle vehicle = validVehicle();
        Dealer newDealer = dealer();

        vehicle.update("Toyota", "Corolla", Set.of(FuelType.FLEX), "Black", Year.now().getValue() + 1,
                BigDecimal.ZERO);
        vehicle.assignDealer(newDealer);

        assertThat(vehicle.getBrand()).isEqualTo("Toyota");
        assertThat(vehicle.getDealer()).isSameAs(newDealer);
    }

    @Test
    void acceptsNullOptionalYearAndPrice() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "Honda", "Civic", Set.of(FuelType.FLEX), "White", null,
                null, dealer());

        assertThat(vehicle.getYear()).isNull();
        assertThat(vehicle.getPrice()).isNull();
    }

    @Test
    void rejectsMissingBrand() {
        assertInvalid(null, "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsBlankBrand() {
        assertInvalid("", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsMissingModel() {
        assertInvalid("Honda", "", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsNullModel() {
        assertInvalid("Honda", null, Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsMissingFuelTypes() {
        assertInvalid("Honda", "Civic", Set.of(), "White", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsNullFuelTypes() {
        assertInvalid("Honda", "Civic", null, "White", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsMissingColor() {
        assertInvalid("Honda", "Civic", Set.of(FuelType.FLEX), "", 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsNullColor() {
        assertInvalid("Honda", "Civic", Set.of(FuelType.FLEX), null, 2024, BigDecimal.ONE);
    }

    @Test
    void rejectsYearBeforeFirstAutomobile() {
        assertInvalid("Honda", "Civic", Set.of(FuelType.FLEX), "White", 1885, BigDecimal.ONE);
    }

    @Test
    void rejectsYearTooFarInFuture() {
        assertInvalid("Honda", "Civic", Set.of(FuelType.FLEX), "White", Year.now().getValue() + 2, BigDecimal.ONE);
    }

    @Test
    void rejectsNegativePrice() {
        assertInvalid("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE.negate());
    }

    @Test
    void rejectsNullDealerAssignment() {
        assertThatThrownBy(() -> validVehicle().assignDealer(null)).isInstanceOf(DomainException.class);
    }

    private void assertInvalid(String brand, String model, Set<FuelType> fuelTypes, String color, Integer year,
                               BigDecimal price) {
        assertThatThrownBy(() -> new Vehicle(UUID.randomUUID(), brand, model, fuelTypes, color, year, price, dealer()))
                .isInstanceOf(DomainException.class);
    }

    private Vehicle validVehicle() {
        return new Vehicle(UUID.randomUUID(), "Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024,
                BigDecimal.ONE, dealer());
    }

    private Dealer dealer() {
        return new Dealer(UUID.randomUUID(), "Driveflow Motors", "12345678000195", "01001000", "Rua A", "10");
    }
}
