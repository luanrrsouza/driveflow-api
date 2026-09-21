package com.dev.driveflowapi.infrastructure.persistence.repository;

import com.dev.driveflowapi.infrastructure.persistence.entity.DealerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:dealer-repository-test;MODE=PostgreSQL;NON_KEYWORDS=YEAR;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DealerJpaRepositoryIntegrationTest {

    @Autowired private DealerJpaRepository dealerRepository;

    @Test
    void rejectsDuplicateCnpjAtDatabaseLevel() {
        dealerRepository.saveAndFlush(dealer("12345678000195"));

        assertThatThrownBy(() -> dealerRepository.saveAndFlush(dealer("12345678000195")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private DealerEntity dealer(String cnpj) {
        return new DealerEntity(UUID.randomUUID(), "Driveflow Motors", cnpj, "01001000", "Rua A", "10");
    }
}
