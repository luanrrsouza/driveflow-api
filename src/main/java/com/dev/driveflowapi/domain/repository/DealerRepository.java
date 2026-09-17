package com.dev.driveflowapi.domain.repository;

import com.dev.driveflowapi.domain.model.Dealer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DealerRepository {

    Dealer save(Dealer dealer);
    Optional<Dealer> findById(UUID id);
    List<Dealer> findAll();
    void deleteById(UUID id);
}
