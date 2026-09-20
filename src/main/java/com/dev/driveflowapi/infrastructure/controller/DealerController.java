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
    @Operation(
            summary = "Create a dealer",
            description = "Creates a new dealer and automatically retrieves the address from the provided zip code."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Dealer created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Zip code not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<DealerResponse> create(
            @Valid @RequestBody CreateDealerRequest request
    ) {

        CreateDealerInput input =
                dealerControllerMapper.toInput(request);

        DealerOutput output =
                dealerService.createDealer(input);

        DealerResponse response =
                dealerControllerMapper.toResponse(output);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find dealer by id",
            description = "Returns a dealer identified by its UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dealer found successfully"
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
    public ResponseEntity<DealerResponse> findById(
            @PathVariable UUID id
    ) {

        DealerOutput output =
                dealerService.findById(id);

        DealerResponse response =
                dealerControllerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "List all dealers",
            description = "Returns all registered dealers."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dealers returned successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<List<DealerResponse>> findAll() {

        List<DealerResponse> response = dealerService
                .findAll()
                .stream()
                .map(dealerControllerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a dealer",
            description = "Updates an existing dealer and refreshes its address using the provided zip code."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dealer updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer or zip code not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<DealerResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDealerRequest request
    ) {

        UpdateDealerInput input =
                dealerControllerMapper.toInput(request);

        DealerOutput output =
                dealerService.updateDealer(id, input);

        DealerResponse response =
                dealerControllerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a dealer",
            description = "Deletes an existing dealer by its UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Dealer deleted successfully"
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
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {

        dealerService.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}