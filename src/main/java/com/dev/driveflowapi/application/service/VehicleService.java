package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.input.vehicle.UpdateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.application.mapper.VehicleMapper;
import com.dev.driveflowapi.domain.exception.DealerNotFoundException;
import com.dev.driveflowapi.domain.exception.VehicleNotFoundException;
import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.model.Vehicle;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import com.dev.driveflowapi.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleOutput createVehicle(CreateVehicleInput input) {
        Dealer dealer = dealerRepository
                .findById(input.dealerId())
                .orElseThrow(DealerNotFoundException::new);

        Vehicle vehicle = vehicleMapper.toDomain(input, dealer);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toOutput(savedVehicle);

    }

    public VehicleOutput findById(UUID vehicleId) {

        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(VehicleNotFoundException::new);

        return vehicleMapper.toOutput(vehicle);
    }

    public List<VehicleOutput> findAll() {

        return vehicleRepository
                .findAll()
                .stream()
                .map(vehicleMapper::toOutput)
                .toList();
    }

    public VehicleOutput updateVehicle(
            UUID vehicleId,
            UpdateVehicleInput input
    ) {

        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(VehicleNotFoundException::new);

        Dealer dealer = dealerRepository
                .findById(input.dealerId())
                .orElseThrow(DealerNotFoundException::new);

        vehicle.update(
                input.brand(),
                input.model(),
                input.fuelTypes(),
                input.color(),
                input.year(),
                input.price()
        );

        vehicle.assignDealer(dealer);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toOutput(updatedVehicle);
    }

    public void deleteById(UUID vehicleId) {
        vehicleRepository
                .findById(vehicleId)
                .orElseThrow(VehicleNotFoundException::new);

        vehicleRepository.deleteById(vehicleId);
    }

    public List<VehicleOutput> findByDealerId(UUID dealerId) {

        dealerRepository
                .findById(dealerId)
                .orElseThrow(DealerNotFoundException::new);

        return vehicleRepository
                .findByDealerId(dealerId)
                .stream()
                .map(vehicleMapper::toOutput)
                .toList();
    }

}