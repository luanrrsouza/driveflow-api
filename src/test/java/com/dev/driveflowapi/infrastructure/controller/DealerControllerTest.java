package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
import com.dev.driveflowapi.application.dto.input.dealer.UpdateDealerInput;
import com.dev.driveflowapi.application.dto.output.dealer.DealerOutput;
import com.dev.driveflowapi.application.service.DealerService;
import com.dev.driveflowapi.infrastructure.controller.dto.request.dealer.CreateDealerRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.request.dealer.UpdateDealerRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.response.DealerResponse;
import com.dev.driveflowapi.infrastructure.controller.mapper.DealerControllerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealerControllerTest {
    @Mock private DealerService dealerService;
    @Mock private DealerControllerMapper mapper;
    @InjectMocks private DealerController controller;

    @Test
    void createsDealer() {
        CreateDealerRequest request = new CreateDealerRequest("Motors", "12345678000195", "01001000", "10");
        CreateDealerInput input = new CreateDealerInput("Motors", "12345678000195", "01001000", "10");
        DealerOutput output = output();
        DealerResponse response = response(output);
        when(mapper.toInput(request)).thenReturn(input);
        when(dealerService.createDealer(input)).thenReturn(output);
        when(mapper.toResponse(output)).thenReturn(response);

        assertThat(controller.create(request).getStatusCode().value()).isEqualTo(201);
        assertThat(controller.create(request).getBody()).isEqualTo(response);
    }

    @Test
    void findsDealerById() {
        DealerOutput output = output();
        when(dealerService.findById(output.id())).thenReturn(output);
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.findById(output.id()).getBody()).isEqualTo(response(output));
    }

    @Test
    void listsDealers() {
        DealerOutput output = output();
        DealerResponse response = response(output);
        when(dealerService.findAll()).thenReturn(List.of(output));
        when(mapper.toResponse(output)).thenReturn(response);

        assertThat(controller.findAll().getBody()).containsExactly(response);
    }

    @Test
    void updatesDealer() {
        DealerOutput output = output();
        UpdateDealerRequest request = new UpdateDealerRequest("Motors", "12345678000195", "01001000", "10");
        UpdateDealerInput input = new UpdateDealerInput("Motors", "12345678000195", "01001000", "10");
        when(mapper.toInput(request)).thenReturn(input);
        when(dealerService.updateDealer(output.id(), input)).thenReturn(output);
        when(mapper.toResponse(output)).thenReturn(response(output));

        assertThat(controller.update(output.id(), request).getBody()).isEqualTo(response(output));
    }

    @Test
    void deletesDealer() {
        UUID id = UUID.randomUUID();

        assertThat(controller.delete(id).getStatusCode().value()).isEqualTo(204);
        verify(dealerService).deleteById(id);
    }

    private DealerOutput output() { return new DealerOutput(UUID.randomUUID(), "Motors", "12345678000195", "01001000", "Rua A", "10"); }
    private DealerResponse response(DealerOutput output) { return new DealerResponse(output.id(), output.corporateName(), output.cnpj(), output.zipCode(), output.address(), output.number()); }
}
