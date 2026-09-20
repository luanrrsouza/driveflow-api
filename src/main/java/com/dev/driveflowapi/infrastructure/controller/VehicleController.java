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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Create a vehicle",
            description = "Creates a new vehicle associated with an existing dealer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Vehicle created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
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
    @Operation(
            summary = "Find vehicle by id",
            description = "Returns a vehicle identified by its UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicle found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
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
    @Operation(
            summary = "List all vehicles",
            description = "Returns all registered vehicles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicles returned successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<List<VehicleResponse>> findAll() {

        List<VehicleResponse> response = vehicleService
                .findAll()
                .stream()
                .map(vehicleControllerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a vehicle",
            description = "Updates an existing vehicle and its dealer association."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicle updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle or dealer not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
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
    @Operation(
            summary = "Delete a vehicle",
            description = "Deletes an existing vehicle by its UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Vehicle deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {

        vehicleService.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(params = "dealerId")
    @Operation(
            summary = "List vehicles by dealer",
            description = "Returns all vehicles associated with the specified dealer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicles returned successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
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