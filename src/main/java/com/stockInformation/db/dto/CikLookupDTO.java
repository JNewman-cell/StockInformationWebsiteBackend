package com.stockInformation.db.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for CikLookup API responses and requests
 */
@Schema(description = "CIK lookup information for a company")
public record CikLookupDTO(

    @Schema(description = "Central Index Key (CIK) number assigned by SEC", example = "320193")
    @NotNull(message = "CIK is required")
    @Positive(message = "CIK must be positive")
    Integer cik,

    @Schema(description = "Company name associated with the CIK", example = "Apple Inc.")
    @NotBlank(message = "Company name is required")
    String companyName,

    @Schema(description = "Timestamp when the record was created", example = "2023-01-01T10:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the record was last updated", example = "2023-01-01T10:00:00")
    LocalDateTime lastUpdatedAt

) {

    // Custom constructor for test convenience
    public CikLookupDTO(Integer cik, String companyName) {
        this(cik, companyName, null, null);
    }
}
