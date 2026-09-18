package com.dev.driveflowapi.infrastructure.persistence.repository;

import com.dev.driveflowapi.infrastructure.persistence.entity.VehicleEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleJpaRepository
        extends JpaRepository<VehicleEntity, UUID> {

    List<VehicleEntity> findByDealer_Id(UUID dealerId);
}