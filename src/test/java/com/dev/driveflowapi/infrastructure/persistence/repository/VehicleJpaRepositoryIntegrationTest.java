package com.dev.driveflowapi.infrastructure.persistence.repository;

import com.dev.driveflowapi.domain.model.FuelType;
import com.dev.driveflowapi.infrastructure.persistence.entity.DealerEntity;
import com.dev.driveflowapi.infrastructure.persistence.entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:repository-test;MODE=PostgreSQL;NON_KEYWORDS=YEAR;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleJpaRepositoryIntegrationTest {

    @Autowired private VehicleJpaRepository vehicleRepository;
    @Autowired private DealerJpaRepository dealerRepository;
    @Autowired private EntityManager entityManager;

    @Test
    void persistsFuelTypesAndFindsVehiclesByDealer() {
        DealerEntity dealer = dealerRepository.save(new DealerEntity(
                UUID.randomUUID(), "Driveflow Motors", "12345678000195", "01001000", "Rua A", "10"));
        VehicleEntity vehicle = new VehicleEntity(
                UUID.randomUUID(), "Honda", "Civic", Set.of(FuelType.FLEX, FuelType.GASOLINE), "White", 2024,
                new BigDecimal("120000.00"), dealer);

        vehicleRepository.saveAndFlush(vehicle);
        entityManager.clear();

        var vehicles = vehicleRepository.findByDealer_Id(dealer.getId());

        assertThat(vehicles).hasSize(1);
        assertThat(vehicles.getFirst().getFuelTypes()).containsExactlyInAnyOrder(FuelType.FLEX, FuelType.GASOLINE);
        assertThat(vehicleRepository.findByDealer_Id(UUID.randomUUID())).isEmpty();
    }
}
