package com.stockInformation.repository;

import com.stockInformation.db.entity.CikLookup;
import com.stockInformation.db.repository.CikLookupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CikLookupRepositoryTest {

    @Autowired
    private CikLookupRepository cikLookupRepository;

    @Test
    void testSaveAndFindByCik() {
        // Given
        CikLookup cikLookup = new CikLookup(320193, "Apple Inc.");

        // When
        CikLookup saved = cikLookupRepository.save(cikLookup);
        Optional<CikLookup> found = cikLookupRepository.findByCik(320193);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCik()).isEqualTo(320193);
        assertThat(found.get().getCompanyName()).isEqualTo("Apple Inc.");
    }
}