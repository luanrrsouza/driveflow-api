package com.dev.driveflowapi.infrastructure.persistence.adapter;

import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.model.FuelType;
import com.dev.driveflowapi.domain.model.Vehicle;
import com.dev.driveflowapi.infrastructure.persistence.entity.VehicleEntity;
import com.dev.driveflowapi.infrastructure.persistence.mapper.VehiclePersistenceMapper;
import com.dev.driveflowapi.infrastructure.persistence.repository.VehicleJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleRepositoryAdapterTest {
    @Mock private VehicleJpaRepository jpaRepository;
    @Mock private VehiclePersistenceMapper mapper;
    @InjectMocks private VehicleRepositoryAdapter adapter;

    @Test
    void savesAndMapsVehicle() {
        Vehicle vehicle = vehicle();
        VehicleEntity entity = new VehicleEntity();
        when(mapper.toEntity(vehicle)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertThat(adapter.save(vehicle)).isSameAs(vehicle);
    }

    @Test
    void findsExistingVehicle() {
        UUID id = UUID.randomUUID();
        VehicleEntity entity = new VehicleEntity();
        Vehicle vehicle = vehicle();
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertThat(adapter.findById(id)).containsSame(vehicle);
    }

    @Test
    void returnsEmptyWhenVehicleDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(adapter.findById(id)).isEmpty();
        verifyNoInteractions(mapper);
    }

    @Test
    void findsAndMapsAllVehicles() {
        VehicleEntity entity = new VehicleEntity();
        Vehicle vehicle = vehicle();
        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertThat(adapter.findAll()).containsExactly(vehicle);
    }

    @Test
    void findsVehiclesByDealerId() {
        UUID dealerId = UUID.randomUUID();
        VehicleEntity entity = new VehicleEntity();
        Vehicle vehicle = vehicle();
        when(jpaRepository.findByDealer_Id(dealerId)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vehicle);

        assertThat(adapter.findByDealerId(dealerId)).containsExactly(vehicle);
    }

    @Test
    void deletesVehicleById() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpaRepository).deleteById(id);
    }

    private Vehicle vehicle() {
        Dealer dealer = new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "01001000", "Rua A", "10");
        return new Vehicle(UUID.randomUUID(), "Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024,
                BigDecimal.ONE, dealer);
    }
}
