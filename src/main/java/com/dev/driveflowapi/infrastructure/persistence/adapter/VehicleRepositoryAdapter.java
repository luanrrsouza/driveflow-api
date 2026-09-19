package com.dev.driveflowapi.infrastructure.persistence.adapter;

import com.dev.driveflowapi.domain.model.Vehicle;
import com.dev.driveflowapi.domain.repository.VehicleRepository;
import com.dev.driveflowapi.infrastructure.persistence.entity.VehicleEntity;
import com.dev.driveflowapi.infrastructure.persistence.mapper.VehiclePersistenceMapper;
import com.dev.driveflowapi.infrastructure.persistence.repository.VehicleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;
    private final VehiclePersistenceMapper vehiclePersistenceMapper;

    @Override
    public Vehicle save(Vehicle vehicle) {

        VehicleEntity entity =
                vehiclePersistenceMapper.toEntity(vehicle);

        VehicleEntity savedEntity =
                vehicleJpaRepository.save(entity);

        return vehiclePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> findById(UUID id) {

        return vehicleJpaRepository
                .findById(id)
                .map(vehiclePersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {

        return vehicleJpaRepository
                .findAll()
                .stream()
                .map(vehiclePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findByDealerId(UUID dealerId) {

        return vehicleJpaRepository
                .findByDealer_Id(dealerId)
                .stream()
                .map(vehiclePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        vehicleJpaRepository.deleteById(id);
    }
}