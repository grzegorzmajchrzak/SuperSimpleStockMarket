package stock.market.impl;

import java.math.BigDecimal;
import java.util.function.Function;

public class PreferredDividedFactorFunction implements Function<StockData, BigDecimal> {
    @Override
    public BigDecimal apply(StockData stockData) {
        return stockData.getParValue().multiply(stockData.getFixedDividend());
    }
}
