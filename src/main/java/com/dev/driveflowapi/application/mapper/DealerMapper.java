package com.dev.driveflowapi.application.mapper;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
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
    Dealer toDomain(CreateDealerInput input);

    DealerOutput toOutput(Dealer dealer);
}
