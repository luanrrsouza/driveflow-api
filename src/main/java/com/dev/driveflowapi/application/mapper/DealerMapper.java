package com.dev.driveflowapi.application.mapper;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.dto.output.dealer.DealerOutput;
import com.dev.driveflowapi.domain.model.Dealer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DealerMapper {

    @Mapping(
            target = "id",
            expression = "java(java.util.UUID.randomUUID())"
    )
    @Mapping(
            target = "zipCode",
            source = "address.zipCode"
    )
    @Mapping(
            target = "address",
            source = "address.address"
    )
    Dealer toDomain(
            CreateDealerInput input,
            AddressOutput address
    );

    DealerOutput toOutput(Dealer dealer);
}
