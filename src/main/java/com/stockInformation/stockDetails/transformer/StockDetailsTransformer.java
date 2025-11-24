package com.stockInformation.stockDetails.transformer;
import com.stockInformation.stockDetails.dto.Dividend;
import com.stockInformation.stockDetails.dto.Growth;
import com.stockInformation.stockDetails.dto.Margins;
import com.stockInformation.stockDetails.dto.DetailsSummaryResponse;
import com.stockInformation.stockDetails.dto.Valuation;
import com.stockInformation.stockDetails.dto.MovingAverage;
import com.stockInformation.stockDetails.entity.TickerOverview;
import com.stockInformation.tickerSummary.entity.TickerSummary;

public class StockDetailsTransformer {

	public static DetailsSummaryResponse buildStockDetailsSummary(TickerSummary summary, TickerOverview overview) {
		MovingAverage fiftyDayAverage = MovingAverage.of(summary.getFiftyDayAverage(), summary.getPreviousClose());
		MovingAverage twoHundredDayAverage = MovingAverage.of(summary.getTwoHundredDayAverage(), summary.getPreviousClose());
		
		Valuation valuation = new Valuation(
			summary.getMarketCap(),
			summary.getPeRatio(),
			summary.getForwardPeRatio(),
			overview.getEnterpriseToEbitda(),
			overview.getPriceToBook(),
			fiftyDayAverage,
			twoHundredDayAverage
		);

		Margins margins = new Margins(
			overview.getGrossMargin(),
			overview.getOperatingMargin(),
			overview.getProfitMargin(),
			overview.getEbitdaMargin()
		);

		Growth growth = Growth.of(
			overview.getEarningsGrowth(),
			overview.getRevenueGrowth(),
			overview.getTrailingEps(),
			overview.getForwardEps(),
			overview.getPegRatio()
		);

		Dividend dividend = null;
		if (summary.getDividendYield() != null || summary.getPayoutRatio() != null) {
			dividend = new Dividend(
				summary.getDividendYield(),
				summary.getPayoutRatio()
			);
		}

		return new DetailsSummaryResponse(valuation, margins, growth, dividend);
	}

}
