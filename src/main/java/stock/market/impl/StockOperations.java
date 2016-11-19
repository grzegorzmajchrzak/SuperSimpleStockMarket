package stock.market.impl;

import stock.market.Trade;

import java.math.BigDecimal;


public interface StockOperations {
    BigDecimal calculateDividedYield(BigDecimal price);
    BigDecimal calculatePERatio(BigDecimal price);
    void record(Trade trade);
    BigDecimal calculateVolumeWeightedStockPrice();
}
