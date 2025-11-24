package com.stockInformation.tickerSummary.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stockInformation.cikLookup.entity.CikLookup;

@Entity
@Table(name = "ticker_summary")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "cikLookup")
@EqualsAndHashCode(of = "ticker")
public class TickerSummary {

    @Id
    @NotBlank(message = "Ticker symbol is required")
    @Size(max = 20, message = "Ticker symbol cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String ticker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cik", referencedColumnName = "cik")
    @JsonIgnore
    private CikLookup cikLookup;

    @Min(value = 0, message = "Market cap must be non-negative")
    @Column(name = "market_cap", nullable = false)
    private Long marketCap;

    @NotNull(message = "Previous close is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Previous close must be positive")
    @Digits(integer = 13, fraction = 2, message = "Previous close format is invalid")
    @Column(name = "previous_close", nullable = false, precision = 15, scale = 2)
    private BigDecimal previousClose;

    @DecimalMin(value = "0.0", message = "PE ratio must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "PE ratio format is invalid")
    @Column(name = "pe_ratio", precision = 10, scale = 2)
    private BigDecimal peRatio;

    @DecimalMin(value = "0.0", message = "Forward PE ratio must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Forward PE ratio format is invalid")
    @Column(name = "forward_pe_ratio", precision = 10, scale = 2)
    private BigDecimal forwardPeRatio;

    @DecimalMin(value = "0.0", message = "Dividend yield must be non-negative")
    @DecimalMax(value = "999.99", message = "Dividend yield cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Dividend yield format is invalid")
    @Column(name = "dividend_yield", precision = 5, scale = 2)
    private BigDecimal dividendYield;

    @DecimalMin(value = "0.0", message = "Payout ratio must be non-negative")
    @DecimalMax(value = "999.99", message = "Payout ratio cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Payout ratio format is invalid")
    @Column(name = "payout_ratio", precision = 5, scale = 2)
    private BigDecimal payoutRatio;

    @DecimalMin(value = "0.0", message = "Annual dividend growth must be non-negative")
    @DecimalMax(value = "999.99", message = "Annual dividend growth cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Annual dividend growth format is invalid")
    @Column(name = "annual_dividend_growth", precision = 5, scale = 2)
    private BigDecimal annualDividendGrowth;

    @DecimalMin(value = "0.0", message = "Five-year average dividend yield must be non-negative")
    @DecimalMax(value = "999.99", message = "Five-year average dividend yield cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Five-year average dividend yield format is invalid")
    @Column(name = "five_year_avg_dividend_yield", precision = 5, scale = 2)
    private BigDecimal fiveYearAvgDividendYield;

    @DecimalMin(value = "0.0", message = "50-day average must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "50-day average format is invalid")
    @Column(name = "fifty_day_average", nullable = false, precision = 10, scale = 2)
    private BigDecimal fiftyDayAverage;

    @DecimalMin(value = "0.0", message = "200-day average must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "200-day average format is invalid")
    @Column(name = "two_hundred_day_average", nullable = false, precision = 10, scale = 2)
    private BigDecimal twoHundredDayAverage;

    // Custom constructor for common use case
    public TickerSummary(String ticker, BigDecimal previousClose) {
        this.ticker = ticker;
        this.previousClose = previousClose;
    }

    // Custom getter/setter for CIK to handle relationship
    public Integer getCik() {
        return cikLookup != null ? cikLookup.getCik() : null;
    }

    public void setCik(Integer cik) {
        if (cik != null) {
            this.cikLookup = new CikLookup();
            this.cikLookup.setCik(cik);
        } else {
            this.cikLookup = null;
        }
    }
}