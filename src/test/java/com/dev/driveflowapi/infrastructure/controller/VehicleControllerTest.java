package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.input.vehicle.UpdateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.application.service.VehicleService;
import com.dev.driveflowapi.domain.model.FuelType;
import com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle.CreateVehicleRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle.UpdateVehicleRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.response.VehicleResponse;
import com.dev.driveflowapi.infrastructure.controller.mapper.VehicleControllerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {
    @Mock private VehicleService vehicleService;
    @Mock private VehicleControllerMapper mapper;
    @InjectMocks private VehicleController controller;

    @Test
    void createsVehicle() {
        VehicleOutput output = output();
        CreateVehicleRequest request = createRequest(output.dealerId());
        CreateVehicleInput input = createInput(output.dealerId());
        when(mapper.toInput(request)).thenReturn(input);
        when(vehicleService.createVehicle(input)).thenReturn(output);
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.create(request).getStatusCode().value()).isEqualTo(201);
        assertThat(controller.create(request).getBody()).isEqualTo(response(output));
    }

    @Test
    void findsVehicleById() {
        VehicleOutput output = output();
        when(vehicleService.findById(output.id())).thenReturn(output);
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.findById(output.id()).getBody()).isEqualTo(response(output));
    }

    @Test
    void listsVehicles() {
        VehicleOutput output = output();
        when(vehicleService.findAll()).thenReturn(List.of(output));
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.findAll().getBody()).containsExactly(response(output));
    }

    @Test
    void updatesVehicle() {
        VehicleOutput output = output();
        UpdateVehicleRequest request = updateRequest(output.dealerId());
        UpdateVehicleInput input = updateInput(output.dealerId());
        when(mapper.toInput(request)).thenReturn(input);
        when(vehicleService.updateVehicle(output.id(), input)).thenReturn(output);
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.update(output.id(), request).getBody()).isEqualTo(response(output));
    }

    @Test
    void deletesVehicle() {
        UUID id = UUID.randomUUID();

        assertThat(controller.delete(id).getStatusCode().value()).isEqualTo(204);
        verify(vehicleService).deleteById(id);
    }

    @Test
    void listsVehiclesByDealer() {
        VehicleOutput output = output();
        when(vehicleService.findByDealerId(output.dealerId())).thenReturn(List.of(output));
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.findByDealerId(output.dealerId()).getBody()).containsExactly(response(output));
    }

    private CreateVehicleRequest createRequest(UUID dealerId) { return new CreateVehicleRequest("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE, dealerId); }
    private UpdateVehicleRequest updateRequest(UUID dealerId) { return new UpdateVehicleRequest("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE, dealerId); }
    private CreateVehicleInput createInput(UUID dealerId) { return new CreateVehicleInput("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE, dealerId); }
    private UpdateVehicleInput updateInput(UUID dealerId) { return new UpdateVehicleInput("Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE, dealerId); }
    private VehicleOutput output() { UUID dealerId = UUID.randomUUID(); return new VehicleOutput(UUID.randomUUID(), "Honda", "Civic", Set.of(FuelType.FLEX), "White", 2024, BigDecimal.ONE, dealerId, "Motors"); }
    private VehicleResponse response(VehicleOutput output) { return new VehicleResponse(output.id(), output.brand(), output.model(), output.fuelTypes(), output.color(), output.year(), output.price(), output.dealerId(), output.dealerName()); }
}
