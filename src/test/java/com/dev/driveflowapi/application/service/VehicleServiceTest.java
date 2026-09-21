package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.input.vehicle.UpdateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.application.mapper.VehicleMapper;
import com.dev.driveflowapi.domain.exception.DealerNotFoundException;
import com.dev.driveflowapi.domain.exception.VehicleNotFoundException;
import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.model.FuelType;
import com.dev.driveflowapi.domain.model.Vehicle;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import com.dev.driveflowapi.domain.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock private VehicleRepository vehicleRepository;
    @Mock private DealerRepository dealerRepository;
    @Mock private VehicleMapper vehicleMapper;
    @InjectMocks private VehicleService vehicleService;

    @Test
    void createsVehicleAssociatedWithExistingDealer() {
        UUID dealerId = UUID.randomUUID();
        Dealer dealer = dealer(dealerId);
        CreateVehicleInput input = createInput(dealerId);
        Vehicle vehicle = vehicle(dealer);
        VehicleOutput expected = output(vehicle, dealer);

        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(vehicleMapper.toDomain(input, dealer)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toOutput(vehicle)).thenReturn(expected);

        assertThat(vehicleService.createVehicle(input)).isEqualTo(expected);

        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).toDomain(input, dealer);
    }

    @Test
    void rejectsCreationWhenDealerDoesNotExist() {
        UUID dealerId = UUID.randomUUID();
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.createVehicle(createInput(dealerId)))
                .isInstanceOf(DealerNotFoundException.class);

        verifyNoInteractions(vehicleRepository, vehicleMapper);
    }

    @Test
    void updatesVehicleAndChangesItsDealer() {
        Dealer previousDealer = dealer(UUID.randomUUID());
        Dealer newDealer = dealer(UUID.randomUUID());
        Vehicle vehicle = vehicle(previousDealer);
        UpdateVehicleInput input = new UpdateVehicleInput(
                "Toyota", "Corolla", Set.of(FuelType.FLEX), "Black", 2025,
                new BigDecimal("155000.00"), newDealer.getId());
        VehicleOutput expected = output(vehicle, newDealer);

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
        when(dealerRepository.findById(newDealer.getId())).thenReturn(Optional.of(newDealer));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toOutput(vehicle)).thenReturn(expected);

        assertThat(vehicleService.updateVehicle(vehicle.getId(), input)).isEqualTo(expected);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());
        assertThat(captor.getValue().getDealer()).isSameAs(newDealer);
        assertThat(captor.getValue().getBrand()).isEqualTo("Toyota");
    }

    @Test
    void rejectsUpdateWhenVehicleDoesNotExist() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.updateVehicle(vehicleId, updateInput(UUID.randomUUID())))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    void rejectsUpdateWhenTargetDealerDoesNotExist() {
        Vehicle vehicle = vehicle(dealer(UUID.randomUUID()));
        UUID targetDealerId = UUID.randomUUID();
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
        when(dealerRepository.findById(targetDealerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.updateVehicle(vehicle.getId(), updateInput(targetDealerId)))
                .isInstanceOf(DealerNotFoundException.class);

        verify(vehicleRepository, never()).save(any());
        verifyNoInteractions(vehicleMapper);
    }

    @Test
    void returnsVehicleById() {
        Vehicle vehicle = vehicle(dealer(UUID.randomUUID()));
        VehicleOutput expected = output(vehicle, vehicle.getDealer());
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toOutput(vehicle)).thenReturn(expected);

        assertThat(vehicleService.findById(vehicle.getId())).isEqualTo(expected);
    }

    @Test
    void rejectsLookupWhenVehicleDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.findById(id))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    void returnsAllVehiclesMappedToOutputs() {
        Dealer dealer = dealer(UUID.randomUUID());
        Vehicle first = vehicle(dealer);
        Vehicle second = vehicle(dealer);
        when(vehicleRepository.findAll()).thenReturn(List.of(first, second));
        when(vehicleMapper.toOutput(first)).thenReturn(output(first, dealer));
        when(vehicleMapper.toOutput(second)).thenReturn(output(second, dealer));

        assertThat(vehicleService.findAll()).containsExactly(output(first, dealer), output(second, dealer));
    }

    @Test
    void deletesExistingVehicle() {
        Vehicle vehicle = vehicle(dealer(UUID.randomUUID()));
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

        vehicleService.deleteById(vehicle.getId());

        verify(vehicleRepository).deleteById(vehicle.getId());
    }

    @Test
    void rejectsDeletionWhenVehicleDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.deleteById(id))
                .isInstanceOf(VehicleNotFoundException.class);

        verify(vehicleRepository, never()).deleteById(any());
    }

    @Test
    void returnsVehiclesForExistingDealer() {
        Dealer dealer = dealer(UUID.randomUUID());
        Vehicle vehicle = vehicle(dealer);
        VehicleOutput expected = output(vehicle, dealer);
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(vehicleRepository.findByDealerId(dealer.getId())).thenReturn(List.of(vehicle));
        when(vehicleMapper.toOutput(vehicle)).thenReturn(expected);

        assertThat(vehicleService.findByDealerId(dealer.getId())).containsExactly(expected);
    }

    @Test
    void rejectsListingVehiclesForUnknownDealer() {
        UUID dealerId = UUID.randomUUID();
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.findByDealerId(dealerId))
                .isInstanceOf(DealerNotFoundException.class);

        verifyNoInteractions(vehicleRepository, vehicleMapper);
    }

    private UpdateVehicleInput updateInput(UUID dealerId) {
        return new UpdateVehicleInput("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024,
                new BigDecimal("120000.00"), dealerId);
    }

    private CreateVehicleInput createInput(UUID dealerId) {
        return new CreateVehicleInput("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024,
                new BigDecimal("120000.00"), dealerId);
    }

    private Dealer dealer(UUID id) {
        return new Dealer(id, "Driveflow Motors", "12345678000195", "01001000", "Rua A", "10");
    }

    private Vehicle vehicle(Dealer dealer) {
        return new Vehicle(UUID.randomUUID(), "Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024,
                new BigDecimal("120000.00"), dealer);
    }

    private VehicleOutput output(Vehicle vehicle, Dealer dealer) {
        return new VehicleOutput(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getFuelTypes(),
                vehicle.getColor(), vehicle.getYear(), vehicle.getPrice(), dealer.getId(), dealer.getCorporateName());
    }
}
