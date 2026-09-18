package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.input.vehicle.UpdateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import com.dev.driveflowapi.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public VehicleOutput createVehicle(
             CreateVehicleInput vehicle
    ) {

    }

    public VehicleOutput findById(
            UUID vehicleId
    ) {
    }

    public List<VehicleOutput> findAll() {
    }

    public VehicleOutput updateVehicle(
            UUID vehicleId,
            UpdateVehicleInput request
    ) {
    }

    public void deleteById(
            UUID vehicleId
    ) {
    }

    public List<VehicleOutput> findByDealerId(
            UUID dealerId
    ) {
    }
}
