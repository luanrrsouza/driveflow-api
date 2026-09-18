package com.dev.driveflowapi.infrastructure.persistence.repository;

import com.dev.driveflowapi.infrastructure.persistence.entity.DealerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DealerJpaRepository
        extends JpaRepository<DealerEntity, UUID> {
}