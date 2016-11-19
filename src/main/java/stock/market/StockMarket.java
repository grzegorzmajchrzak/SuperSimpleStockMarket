package stock.market;

import java.math.BigDecimal;

/**
 * This is <b>facade</b> interface.
 * Created by g on 11/16/2016.
 */
public interface StockMarket {
 
    /**
     * Calculate Divided Yield factor
     * 
     * @param stockSymbol stock symbol supported by the service
     * @param price given price, have to be > 0
     * @return calculated Divided Yield
     * 
     * @throws IllegalArgumentException if the specified stock symbol is not >0 or if stock symbol is empty or not supported
     */
    BigDecimal calculateDividedYield(String stockSymbol, BigDecimal price);

    /**
     * Calculate P/E Ratio factor
     *
     * @param stockSymbol stock symbol supported by the service
     * @param price given price, have to be > 0
     * @return calculated P/E Ratio
     *
     * @throws IllegalArgumentException if the specified stock symbol is not >0 or if stock symbol is empty or not supported
     */
    BigDecimal calculatePERatio(String stockSymbol, BigDecimal price);

    /**
     * Record trade
     *
     * @param trade trade to record
     *
     * @throws IllegalArgumentException if the given trade is not valid
     */
    void record(Trade trade);

    /**
     * Calculate Volume Weighted Stock Price factor
     *
     * @param stockSymbol stock symbol supported by the service
     * @return calculated Volume Weight Stock Price
     *
     * @throws IllegalArgumentException if the specified stock symbol is not >0 or if stock symbol is empty or not supported
     */
    BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol);

    /**
     * Calculate GBCE factor
     *
     * @return GBCE factor
     */
    BigDecimal calculateGBCE();
}
