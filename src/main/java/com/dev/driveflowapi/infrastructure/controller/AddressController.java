package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@Tag(
        name = "Addresses",
        description = "Operations for address lookup"
)
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{zipCode}")
    @Operation(
            summary = "Find address by zip code",
            description = "Returns the address associated with the provided zip code."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Address found successfully"
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
    public ResponseEntity<AddressOutput> findByZipCode(
            @PathVariable String zipCode
    ) {

        AddressOutput output =
                addressService.findByZipCode(zipCode);

        return ResponseEntity.ok(output);
    }
}