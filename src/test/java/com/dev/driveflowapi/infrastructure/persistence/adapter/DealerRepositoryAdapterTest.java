package com.dev.driveflowapi.infrastructure.persistence.adapter;

import com.dev.driveflowapi.domain.model.Dealer;
import com.dev.driveflowapi.infrastructure.persistence.entity.DealerEntity;
import com.dev.driveflowapi.infrastructure.persistence.mapper.DealerPersistenceMapper;
import com.dev.driveflowapi.infrastructure.persistence.repository.DealerJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealerRepositoryAdapterTest {
    @Mock private DealerJpaRepository jpaRepository;
    @Mock private DealerPersistenceMapper mapper;
    @InjectMocks private DealerRepositoryAdapter adapter;

    @Test
    void savesAndMapsDealer() {
        Dealer dealer = dealer();
        DealerEntity entity = new DealerEntity();
        when(mapper.toEntity(dealer)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(dealer);

        assertThat(adapter.save(dealer)).isSameAs(dealer);
    }

    @Test
    void findsExistingDealer() {
        UUID id = UUID.randomUUID();
        DealerEntity entity = new DealerEntity();
        Dealer dealer = dealer();
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(dealer);

        assertThat(adapter.findById(id)).containsSame(dealer);
    }

    @Test
    void returnsEmptyWhenDealerDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(adapter.findById(id)).isEmpty();
        verifyNoInteractions(mapper);
    }

    @Test
    void findsAndMapsAllDealers() {
        DealerEntity entity = new DealerEntity();
        Dealer dealer = dealer();
        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(dealer);

        assertThat(adapter.findAll()).containsExactly(dealer);
    }

    @Test
    void deletesDealerById() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpaRepository).deleteById(id);
    }

    private Dealer dealer() { return new Dealer(UUID.randomUUID(), "Motors", "12345678000195", "01001000", "Rua A", "10"); }
}
