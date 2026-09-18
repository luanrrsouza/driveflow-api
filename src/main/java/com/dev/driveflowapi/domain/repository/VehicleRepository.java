package com.dev.driveflowapi.domain.repository;

import com.dev.driveflowapi.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(UUID id);
    List<Vehicle> findAll();
    void deleteById(UUID id);
    List<Vehicle> findByDealerId(UUID dealerId);
}