package com.dev.driveflowapi.infrastructure.controller.exception;

import com.dev.driveflowapi.domain.exception.DealerNotFoundException;
import com.dev.driveflowapi.domain.exception.DomainException;
import com.dev.driveflowapi.domain.exception.VehicleNotFoundException;
import com.dev.driveflowapi.domain.exception.ZipCodeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsVehicleNotFoundTo404() {
        assertResponse(handler.handleVehicleNotFoundException(new VehicleNotFoundException()), HttpStatus.NOT_FOUND,
                "Vehicle not found.");
    }

    @Test
    void mapsDealerNotFoundTo404() {
        assertResponse(handler.handleDealerNotFoundException(new DealerNotFoundException()), HttpStatus.NOT_FOUND,
                "Dealer not found.");
    }

    @Test
    void mapsZipCodeNotFoundTo404() {
        assertResponse(handler.handleZipCodeNotFoundException(new ZipCodeNotFoundException("00000000")), HttpStatus.NOT_FOUND,
                "Zip code not found: 00000000");
    }

    @Test
    void mapsDomainExceptionTo400() {
        assertResponse(handler.handleDomainException(new DomainException("Invalid input")), HttpStatus.BAD_REQUEST,
                "Invalid input");
    }

    @Test
    void mapsValidationFieldsTo400() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("request", "brand", "Brand is required.")));

        var response = handler.handleValidationException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().fields()).containsEntry("brand", "Brand is required.");
    }

    @Test
    void hidesUnexpectedExceptionDetails() {
        var response = handler.handleUnexpectedException(new IllegalStateException("sensitive detail"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred.");
    }

    private void assertResponse(org.springframework.http.ResponseEntity<ErrorResponse> response, HttpStatus status,
                                String message) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody().message()).isEqualTo(message);
        assertThat(response.getBody().timestamp()).isNotNull();
    }
}
