package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.vehicle.CreateVehicleRequest;
import com.dev.driveflowapi.application.dto.vehicle.UpdateVehicleRequest;
import com.dev.driveflowapi.application.dto.vehicle.VehicleResponse;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import com.dev.driveflowapi.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public VehicleResponse createVehicle(
            CreateVehicleRequest request
    ) {

    }

    public VehicleResponse findById(
            UUID vehicleId
    ) {
    }

    public List<VehicleResponse> findAll() {
    }

    public VehicleResponse updateVehicle(
            UUID vehicleId,
            UpdateVehicleRequest request
    ) {
    }

    public void deleteById(
            UUID vehicleId
    ) {
    }

    public List<VehicleResponse> findByDealerId(
            UUID dealerId
    ) {
    }
}
