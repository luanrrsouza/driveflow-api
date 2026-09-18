package com.dev.driveflowapi.infrastructure.persistence.adapter;

import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import com.dev.driveflowapi.infrastructure.persistence.entity.DealerEntity;
import com.dev.driveflowapi.infrastructure.persistence.mapper.DealerPersistenceMapper;
import com.dev.driveflowapi.infrastructure.persistence.repository.DealerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DealerRepositoryAdapter implements DealerRepository {

    private final DealerJpaRepository dealerJpaRepository;
    private final DealerPersistenceMapper dealerPersistenceMapper;

    @Override
    public Dealer save(Dealer dealer) {

        DealerEntity entity =
                dealerPersistenceMapper.toEntity(dealer);

        DealerEntity savedEntity =
                dealerJpaRepository.save(entity);

        return dealerPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Dealer> findById(UUID id) {

        return dealerJpaRepository
                .findById(id)
                .map(dealerPersistenceMapper::toDomain);
    }

    @Override
    public List<Dealer> findAll() {

        return dealerJpaRepository
                .findAll()
                .stream()
                .map(dealerPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        dealerJpaRepository.deleteById(id);
    }
}