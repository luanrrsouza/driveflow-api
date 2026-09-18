package com.dev.driveflowapi.infrastructure.persistence.mapper;

import com.dev.driveflowapi.domain.model.Vehicle;
import com.dev.driveflowapi.infrastructure.persistence.entity.VehicleEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = DealerPersistenceMapper.class
)
public interface VehiclePersistenceMapper {

    VehicleEntity toEntity(Vehicle vehicle);

    Vehicle toDomain(VehicleEntity entity);
}
