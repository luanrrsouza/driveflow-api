package com.dev.driveflowapi.infrastructure.controller.mapper;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.input.vehicle.UpdateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle.CreateVehicleRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.request.vehicle.UpdateVehicleRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.response.VehicleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleControllerMapper {

    CreateVehicleInput toInput(CreateVehicleRequest request);

    UpdateVehicleInput toInput(UpdateVehicleRequest request);

    VehicleResponse toResponse(VehicleOutput output);
}