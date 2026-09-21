package com.dev.driveflowapi.domain.model;

import com.dev.driveflowapi.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DealerTest {

    @Test
    void createsAndUpdatesValidDealer() {
        Dealer dealer = validDealer();

        dealer.update("New Motors", "98765432000198", "01310930", "Avenida Paulista", "50");

        assertThat(dealer.getCorporateName()).isEqualTo("New Motors");
        assertThat(dealer.getAddress()).isEqualTo("Avenida Paulista");
    }

    @Test
    void rejectsMissingCorporateName() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "", "12345678000195", "01001000", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsNullCorporateName() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), null, "12345678000195", "01001000", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsMissingCnpj() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", null, "01001000", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsBlankCnpj() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "", "01001000", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsCnpjWithInvalidLength() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "123", "01001000", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsMissingZipCode() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsNullZipCode() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", null, "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsZipCodeWithInvalidLength() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "123", "Rua A", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsMissingAddress() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "01001000", "", "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsNullAddress() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "01001000", null, "10"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsMissingAddressNumber() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "01001000", "Rua A", ""))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsNullAddressNumber() {
        assertThatThrownBy(() -> new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "01001000", "Rua A", null))
                .isInstanceOf(DomainException.class);
    }

    private Dealer validDealer() {
        return new Dealer(UUID.randomUUID(), "Driveflow Motors", "12345678000195", "01001000", "Rua A", "10");
    }
}
