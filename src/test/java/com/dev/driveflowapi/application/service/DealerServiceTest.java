package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
import com.dev.driveflowapi.application.dto.input.dealer.UpdateDealerInput;
import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.dto.output.dealer.DealerOutput;
import com.dev.driveflowapi.application.mapper.DealerMapper;
import com.dev.driveflowapi.application.port.out.ZipCodeGateway;
import com.dev.driveflowapi.domain.exception.DealerNotFoundException;
import com.dev.driveflowapi.domain.exception.DomainException;
import com.dev.driveflowapi.domain.exception.ZipCodeNotFoundException;
import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealerServiceTest {

    @Mock private DealerRepository dealerRepository;
    @Mock private DealerMapper dealerMapper;
    @Mock private ZipCodeGateway zipCodeGateway;
    @InjectMocks private DealerService dealerService;

    @Test
    void createsDealerUsingAddressReturnedByZipCodeGateway() {
        CreateDealerInput input = new CreateDealerInput("Driveflow Motors", "12345678000195", "01001000", "10");
        AddressOutput address = new AddressOutput("01001-000", "Praça da Sé, Sé, São Paulo - SP");
        Dealer dealer = dealer();
        DealerOutput expected = output(dealer);

        when(zipCodeGateway.findByZipCode(input.zipCode())).thenReturn(address);
        when(dealerMapper.toDomain(input, address)).thenReturn(dealer);
        when(dealerRepository.save(dealer)).thenReturn(dealer);
        when(dealerMapper.toOutput(dealer)).thenReturn(expected);

        assertThat(dealerService.createDealer(input)).isEqualTo(expected);

        verify(zipCodeGateway).findByZipCode("01001000");
        verify(dealerRepository).save(dealer);
    }

    @Test
    void returnsDealerById() {
        Dealer dealer = dealer();
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(dealerMapper.toOutput(dealer)).thenReturn(output(dealer));

        assertThat(dealerService.findById(dealer.getId()).corporateName()).isEqualTo("Driveflow Motors");
    }

    @Test
    void rejectsLookupForUnknownDealer() {
        when(dealerRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealerService.findById(UUID.randomUUID()))
                .isInstanceOf(DealerNotFoundException.class);
    }

    @Test
    void returnsAllDealersMappedToOutputs() {
        Dealer first = dealer();
        Dealer second = dealer();
        when(dealerRepository.findAll()).thenReturn(List.of(first, second));
        when(dealerMapper.toOutput(first)).thenReturn(output(first));
        when(dealerMapper.toOutput(second)).thenReturn(output(second));

        assertThat(dealerService.findAll()).containsExactly(output(first), output(second));
        verify(dealerRepository).findAll();
    }

    @Test
    void returnsEmptyListWhenThereAreNoDealers() {
        when(dealerRepository.findAll()).thenReturn(List.of());

        assertThat(dealerService.findAll()).isEmpty();

        verify(dealerRepository).findAll();
        verifyNoInteractions(dealerMapper);
    }

    @Test
    void propagatesZipCodeNotFoundWhenCreatingDealerWithoutPersisting() {
        CreateDealerInput input = new CreateDealerInput("Driveflow Motors", "12345678000195", "00000000", "10");
        ZipCodeNotFoundException exception = new ZipCodeNotFoundException(input.zipCode());
        when(zipCodeGateway.findByZipCode(input.zipCode())).thenThrow(exception);

        assertThatThrownBy(() -> dealerService.createDealer(input)).isSameAs(exception);

        verifyNoInteractions(dealerMapper, dealerRepository);
    }

    @Test
    void propagatesPersistenceFailureWhenCreatingDealer() {
        CreateDealerInput input = new CreateDealerInput("Driveflow Motors", "12345678000195", "01001000", "10");
        AddressOutput address = new AddressOutput("01001-000", "Rua Central");
        Dealer dealer = dealer();
        RuntimeException exception = new RuntimeException("Database unavailable");
        when(zipCodeGateway.findByZipCode(input.zipCode())).thenReturn(address);
        when(dealerMapper.toDomain(input, address)).thenReturn(dealer);
        when(dealerRepository.save(dealer)).thenThrow(exception);

        assertThatThrownBy(() -> dealerService.createDealer(input)).isSameAs(exception);
        verify(dealerMapper, never()).toOutput(any());
    }

    @Test
    void updatesDealerUsingRefreshedAddressFromZipCodeGateway() {
        Dealer dealer = dealer();
        UpdateDealerInput input = new UpdateDealerInput("New Motors", "12345678000195", "01310930", "50");
        AddressOutput address = new AddressOutput("01310-930", "Avenida Paulista, Sao Paulo - SP");
        DealerOutput expected = new DealerOutput(dealer.getId(), "New Motors", input.cnpj(), address.zipCode(),
                address.address(), input.number());

        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(zipCodeGateway.findByZipCode(input.zipCode())).thenReturn(address);
        when(dealerRepository.save(dealer)).thenReturn(dealer);
        when(dealerMapper.toOutput(dealer)).thenReturn(expected);

        assertThat(dealerService.updateDealer(dealer.getId(), input)).isEqualTo(expected);

        assertThat(dealer.getCorporateName()).isEqualTo("New Motors");
        assertThat(dealer.getAddress()).isEqualTo(address.address());
        verify(dealerRepository).save(dealer);
    }

    @Test
    void rejectsUpdateWhenDealerDoesNotExistWithoutCallingZipCodeGateway() {
        UUID id = UUID.randomUUID();
        when(dealerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealerService.updateDealer(id,
                new UpdateDealerInput("New Motors", "12345678000195", "01310930", "50")))
                .isInstanceOf(DealerNotFoundException.class);

        verifyNoInteractions(zipCodeGateway, dealerMapper);
        verify(dealerRepository, never()).save(any());
    }

    @Test
    void propagatesZipCodeNotFoundWhenUpdatingWithoutPersisting() {
        Dealer dealer = dealer();
        UpdateDealerInput input = new UpdateDealerInput("New Motors", "12345678000195", "00000000", "50");
        ZipCodeNotFoundException exception = new ZipCodeNotFoundException(input.zipCode());
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(zipCodeGateway.findByZipCode(input.zipCode())).thenThrow(exception);

        assertThatThrownBy(() -> dealerService.updateDealer(dealer.getId(), input)).isSameAs(exception);

        verify(dealerRepository, never()).save(any());
        verifyNoInteractions(dealerMapper);
    }

    @Test
    void doesNotPersistUpdateWhenCorporateNameIsInvalid() {
        assertInvalidUpdate("", "12345678000195", "10", new AddressOutput("01310-930", "Avenida Paulista"));
    }

    @Test
    void doesNotPersistUpdateWhenCnpjIsInvalid() {
        assertInvalidUpdate("New Motors", "123", "10", new AddressOutput("01310-930", "Avenida Paulista"));
    }

    @Test
    void doesNotPersistUpdateWhenAddressReturnedByGatewayIsInvalid() {
        assertInvalidUpdate("New Motors", "12345678000195", "10", new AddressOutput("01310-930", ""));
    }

    @Test
    void doesNotPersistUpdateWhenZipCodeReturnedByGatewayIsInvalid() {
        assertInvalidUpdate("New Motors", "12345678000195", "10", new AddressOutput("123", "Avenida Paulista"));
    }

    @Test
    void doesNotPersistUpdateWhenAddressNumberIsInvalid() {
        assertInvalidUpdate("New Motors", "12345678000195", "", new AddressOutput("01310-930", "Avenida Paulista"));
    }

    @Test
    void propagatesPersistenceFailureWhenUpdatingDealer() {
        Dealer dealer = dealer();
        UpdateDealerInput input = new UpdateDealerInput("New Motors", "12345678000195", "01310930", "50");
        AddressOutput address = new AddressOutput("01310-930", "Avenida Paulista");
        RuntimeException exception = new RuntimeException("Database unavailable");
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(zipCodeGateway.findByZipCode(input.zipCode())).thenReturn(address);
        when(dealerRepository.save(dealer)).thenThrow(exception);

        assertThatThrownBy(() -> dealerService.updateDealer(dealer.getId(), input)).isSameAs(exception);
        verify(dealerMapper, never()).toOutput(any());
    }

    @Test
    void deletesExistingDealer() {
        Dealer dealer = dealer();
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));

        dealerService.deleteById(dealer.getId());

        verify(dealerRepository).deleteById(dealer.getId());
    }

    @Test
    void rejectsDeletionWhenDealerDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(dealerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dealerService.deleteById(id))
                .isInstanceOf(DealerNotFoundException.class);

        verify(dealerRepository, never()).deleteById(any());
    }

    private void assertInvalidUpdate(
            String corporateName,
            String cnpj,
            String number,
            AddressOutput address
    ) {
        Dealer dealer = dealer();
        UpdateDealerInput input = new UpdateDealerInput(corporateName, cnpj, "01310930", number);
        when(dealerRepository.findById(dealer.getId())).thenReturn(Optional.of(dealer));
        when(zipCodeGateway.findByZipCode(input.zipCode())).thenReturn(address);

        assertThatThrownBy(() -> dealerService.updateDealer(dealer.getId(), input))
                .isInstanceOf(DomainException.class);

        verify(dealerRepository, never()).save(any());
        verifyNoInteractions(dealerMapper);
    }

    private Dealer dealer() {
        return new Dealer(UUID.randomUUID(), "Driveflow Motors", "12345678000195", "01001000", "Rua A", "10");
    }

    private DealerOutput output(Dealer dealer) {
        return new DealerOutput(dealer.getId(), dealer.getCorporateName(), dealer.getCnpj(), dealer.getZipCode(),
                dealer.getAddress(), dealer.getNumber());
    }
}
