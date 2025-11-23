package com.stockInformation.stockDetails.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ticker_overview")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "ticker")
public class TickerOverview {

    @Id
    @NotBlank(message = "Ticker symbol is required")
    @Size(max = 7, message = "Ticker symbol cannot exceed 7 characters")
    @Column(nullable = false, length = 7)
    private String ticker;

    @DecimalMin(value = "0.0", message = "Enterprise to EBITDA must be non-negative")
    @Digits(integer = 5, fraction = 2, message = "Enterprise to EBITDA format is invalid")
    @Column(name = "enterprise_to_ebitda", precision = 7, scale = 2)
    private BigDecimal enterpriseToEbitda;

    @DecimalMin(value = "0.0", message = "Price to book must be non-negative")
    @Digits(integer = 5, fraction = 2, message = "Price to book format is invalid")
    @Column(name = "price_to_book", precision = 7, scale = 2)
    private BigDecimal priceToBook;

    @DecimalMin(value = "0.0", message = "Gross margin must be non-negative")
    @DecimalMax(value = "999.99", message = "Gross margin cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Gross margin format is invalid")
    @Column(name = "gross_margin", precision = 5, scale = 2)
    private BigDecimal grossMargin;

    @DecimalMin(value = "0.0", message = "EBITDA margin must be non-negative")
    @DecimalMax(value = "999.99", message = "EBITDA margin cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "EBITDA margin format is invalid")
    @Column(name = "ebitda_margin", precision = 5, scale = 2)
    private BigDecimal ebitdaMargin;

    @DecimalMin(value = "0.0", message = "Operating margin must be non-negative")
    @DecimalMax(value = "999.99", message = "Operating margin cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Operating margin format is invalid")
    @Column(name = "operating_margin", precision = 5, scale = 2)
    private BigDecimal operatingMargin;

    @DecimalMin(value = "0.0", message = "Profit margin must be non-negative")
    @DecimalMax(value = "999.99", message = "Profit margin cannot exceed 999.99%")
    @Digits(integer = 3, fraction = 2, message = "Profit margin format is invalid")
    @Column(name = "profit_margin", precision = 5, scale = 2)
    private BigDecimal profitMargin;

    @Digits(integer = 7, fraction = 2, message = "Earnings growth format is invalid")
    @Column(name = "earnings_growth", precision = 9, scale = 2)
    private BigDecimal earningsGrowth;

    @Digits(integer = 8, fraction = 2, message = "Revenue growth format is invalid")
    @Column(name = "revenue_growth", precision = 10, scale = 2)
    private BigDecimal revenueGrowth;

    @Digits(integer = 5, fraction = 2, message = "Trailing EPS format is invalid")
    @Column(name = "trailing_eps", precision = 7, scale = 2)
    private BigDecimal trailingEps;

    @Digits(integer = 5, fraction = 2, message = "Forward EPS format is invalid")
    @Column(name = "forward_eps", precision = 7, scale = 2)
    private BigDecimal forwardEps;

    @DecimalMin(value = "0.0", message = "PEG ratio must be non-negative")
    @Digits(integer = 6, fraction = 2, message = "PEG ratio format is invalid")
    @Column(name = "peg_ratio", precision = 8, scale = 2)
    private BigDecimal pegRatio;

}
