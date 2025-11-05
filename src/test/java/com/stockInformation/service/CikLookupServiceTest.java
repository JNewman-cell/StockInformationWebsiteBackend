package com.stockInformation.service;

import com.stockInformation.db.entity.CikLookup;
import com.stockInformation.db.repository.CikLookupRepository;
import com.stockInformation.db.service.CikLookupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CikLookupServiceTest {

    @Mock
    private CikLookupRepository cikLookupRepository;

    @InjectMocks
    private CikLookupService cikLookupService;

    @Test
    void testFindByCik() {
        // Given
        CikLookup cikLookup = new CikLookup(320193, "Apple Inc.");
        when(cikLookupRepository.findByCik(320193)).thenReturn(Optional.of(cikLookup));

        // When
        Optional<CikLookup> result = cikLookupService.findByCik(320193);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(cikLookup);
        verify(cikLookupRepository).findByCik(320193);
    }
}