package com.dev.driveflowapi.infrastructure.persistence.mapper;

import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.infrastructure.persistence.entity.DealerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealerPersistenceMapper {

    DealerEntity toEntity(Dealer dealer);

    Dealer toDomain(DealerEntity entity);
}