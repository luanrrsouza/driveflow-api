package com.dev.driveflowapi.application.service;

import com.dev.driveflowapi.application.dto.dealer.CreateDealerRequest;
import com.dev.driveflowapi.application.dto.dealer.DealerResponse;
import com.dev.driveflowapi.application.dto.dealer.UpdateDealerRequest;
import com.dev.driveflowapi.domain.repository.DealerRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
public class DealerService {

    private final DealerRepository dealerRepository;

    public DealerResponse createDealer(
            CreateDealerRequest request
    ) {
    }

    public DealerResponse findById(
            UUID dealerId
    ) {
    }

    public List<DealerResponse> findAll() {
    }

    public DealerResponse updateDealer(
            UUID dealerId,
            UpdateDealerRequest request
    ) {
    }

    public void deleteById(
            UUID dealerId
    ) {
    }
}