package com.stockinfo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cik_lookup")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "cik")
public class CikLookup {

    @Id
    @NotNull(message = "CIK is required")
    @Positive(message = "CIK must be positive")
    @Column(nullable = false)
    private Integer cik;

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }

    // Custom constructor for common use case
    public CikLookup(Integer cik, String companyName) {
        this.cik = cik;
        this.companyName = companyName;
    }
}