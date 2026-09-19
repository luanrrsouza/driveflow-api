package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
import com.dev.driveflowapi.application.dto.input.dealer.UpdateDealerInput;
import com.dev.driveflowapi.application.dto.output.dealer.DealerOutput;
import com.dev.driveflowapi.application.service.DealerService;
import com.dev.driveflowapi.infrastructure.controller.dto.request.dealer.CreateDealerRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.request.dealer.UpdateDealerRequest;
import com.dev.driveflowapi.infrastructure.controller.dto.response.DealerResponse;
import com.dev.driveflowapi.infrastructure.controller.mapper.DealerControllerMapper;
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
@RequestMapping("/dealer")
@RequiredArgsConstructor
@Tag(
        name = "Dealers",
        description = "Operations for dealer management"
)
public class DealerController {

    private final DealerService dealerService;
    private final DealerControllerMapper dealerControllerMapper;

    @PostMapping
    @Operation(summary = "Create a dealer")
    public ResponseEntity<DealerResponse> create(@Valid @RequestBody CreateDealerRequest request){

        CreateDealerInput input = dealerControllerMapper.toInput(request);

        DealerOutput output = dealerService.createDealer(input);

        DealerResponse response = dealerControllerMapper.toResponse(output);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Find dealer by id")
    public ResponseEntity<DealerResponse> findById(@PathVariable UUID id) {

        DealerOutput output = dealerService.findById(id);

        DealerResponse response = dealerControllerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all dealers")
    public ResponseEntity<List<DealerResponse>> findAll() {
        List<DealerResponse> response = dealerService
                .findAll()
                .stream()
                .map(dealerControllerMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a dealer")
    public ResponseEntity<DealerResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateDealerRequest request) {

        UpdateDealerInput input = dealerControllerMapper.toInput(request);
        DealerOutput output = dealerService.updateDealer(id, input);
        DealerResponse response = dealerControllerMapper.toResponse(output);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dealer")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        dealerService.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }


}