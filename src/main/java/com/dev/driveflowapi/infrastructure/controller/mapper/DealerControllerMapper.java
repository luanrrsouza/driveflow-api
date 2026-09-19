package com.dev.driveflowapi.infrastructure.controller.mapper;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
import com.dev.driveflowapi.application.dto.input.dealer.UpdateDealerInput;
import com.dev.driveflowapi.application.dto.output.dealer.DealerOutput;
import com.dev.driveflowapi.infrastructure.controller.dto.request.dealer.CreateDealerRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.request.dealer.UpdateDealerRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.response.DealerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealerControllerMapper {

    CreateDealerInput toInput(CreateDealerRequest request);

    UpdateDealerInput toInput(UpdateDealerRequest request);

    DealerResponse toResponse(DealerOutput output);
}