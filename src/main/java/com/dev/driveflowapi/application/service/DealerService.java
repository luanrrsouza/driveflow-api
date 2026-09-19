package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.input.dealer.CreateDealerInput;
import com.dev.driveflowapi.application.dto.input.dealer.UpdateDealerInput;
import com.dev.driveflowapi.application.dto.output.dealer.DealerOutput;
import com.dev.driveflowapi.application.mapper.DealerMapper;
import com.dev.driveflowapi.domain.exception.DealerNotFoundException;
import com.dev.driveflowapi.domain.exception.DomainException;
import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealerService {

    private final DealerRepository dealerRepository;
    private final DealerMapper dealerMapper;

    public DealerOutput createDealer(CreateDealerInput input) {
        Dealer dealer = dealerMapper.toDomain(input);

        Dealer savedDealer = dealerRepository.save(dealer);

        return dealerMapper.toOutput(savedDealer);
    }

    public DealerOutput findById(UUID dealerId) {

        Dealer dealer = dealerRepository.findById(dealerId).orElseThrow(
                () -> new DomainException("Dealer not found.")
        );

        return dealerMapper.toOutput(dealer);
    }

    public List<DealerOutput> findAll() {

        return dealerRepository
                .findAll()
                .stream()
                .map(dealerMapper::toOutput)
                .toList();
    }

    public DealerOutput updateDealer(UUID dealerId, UpdateDealerInput input)
    {
        Dealer dealer = dealerRepository
                .findById(dealerId)
                .orElseThrow(DealerNotFoundException::new);

        dealer.update(
                input.corporateName(),
                input.cnpj(),
                input.zipCode(),
                input.address()
        );

        Dealer updatedDealer = dealerRepository.save(dealer);

        return dealerMapper.toOutput(updatedDealer);
    }

    public void deleteById(UUID dealerId) {
        dealerRepository
                .findById(dealerId)
                .orElseThrow(DealerNotFoundException::new);

        dealerRepository.deleteById(dealerId);
    }
}