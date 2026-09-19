package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.input.vehicle.UpdateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.application.service.VehicleService;
import com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle.CreateVehicleRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle.UpdateVehicleRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.response.VehicleResponse;
import com.dev.driveflowapi.infrastructure.controller.mapper.VehicleControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(
        name = "Vehicles",
        description = "Operations for vehicle management"
)
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleControllerMapper vehicleControllerMapper;

    @PostMapping
    @Operation(summary = "Create a vehicle")
    public ResponseEntity<VehicleResponse> create(
            @Valid @RequestBody CreateVehicleRequest request
    ) {

        CreateVehicleInput input =
                vehicleControllerMapper.toInput(request);

        VehicleOutput output =
                vehicleService.createVehicle(input);

        VehicleResponse response =
                vehicleControllerMapper.toResponse(output);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find vehicle by id")
    public ResponseEntity<VehicleResponse> findById(
            @PathVariable UUID id
    ) {

        VehicleOutput output =
                vehicleService.findById(id);

        VehicleResponse response =
                vehicleControllerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all vehicles")
    public ResponseEntity<List<VehicleResponse>> findAll() {

        List<VehicleResponse> response = vehicleService
                .findAll()
                .stream()
                .map(vehicleControllerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle")
    public ResponseEntity<VehicleResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {

        UpdateVehicleInput input =
                vehicleControllerMapper.toInput(request);

        VehicleOutput output =
                vehicleService.updateVehicle(id, input);

        VehicleResponse response =
                vehicleControllerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {

        vehicleService.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(params = "dealerId")
    @Operation(summary = "List vehicles by dealer")
    public ResponseEntity<List<VehicleResponse>> findByDealerId(
            @RequestParam UUID dealerId
    ) {

        List<VehicleResponse> response = vehicleService
                .findByDealerId(dealerId)
                .stream()
                .map(vehicleControllerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}