package com.dev.driveflowapi.application.mapper;

import com.dev.driveflowapi.application.dto.input.vehicle.CreateVehicleInput;
import com.dev.driveflowapi.application.dto.output.vehicle.VehicleOutput;
import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "dealer", source = "dealer")
    Vehicle toDomain(
            CreateVehicleInput input,
            Dealer dealer
    );

    @Mapping(target = "dealerId", source = "dealer.id")
    @Mapping(target = "dealerName", source = "dealer.corporateName")
    VehicleOutput toOutput(Vehicle vehicle);
}
