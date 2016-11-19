package stock.market.impl;

import stock.market.Trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * Stock operations with <b>defined precision</b> and <b>rounding policy</b> for calculations
 */
public class BasicStockOperations implements StockOperations {

    private static final int ROUNDING_POLICY = BigDecimal.ROUND_HALF_UP;
    private static final int PRECISION = 16;

    private final TradeStorage tradeStorage;
    private final Function<StockData, BigDecimal> dividedYieldProvider;
    private final StockData stockData;

    public BasicStockOperations(Function<StockData, BigDecimal> dividedYieldProvider, StockData stockData, TradeStorage tradeStorage) {
        this.dividedYieldProvider = dividedYieldProvider;
        this.stockData = stockData;
        this.tradeStorage = tradeStorage;
        validate();
    }

    private void validate() {
        if (dividedYieldProvider == null) {
            throw new IllegalArgumentException("dividedYieldProvider cannot be null");
        }
        if (stockData == null) {
            throw new IllegalArgumentException("stockData cannot be null");
        }
        if (tradeStorage == null) {
            throw new IllegalArgumentException("tradeStorage cannot be null");
        }
    }

    @Override
    public BigDecimal calculateDividedYield(BigDecimal price) {
        return dividedYieldProvider.apply(stockData).divide(price, PRECISION, ROUNDING_POLICY);
    }

    @Override
    public BigDecimal calculatePERatio(BigDecimal price) {
        BigDecimal divisor = calculateDividedYield(price);
        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            return BigDecimal.ZERO;
        }
        return price.divide(divisor, PRECISION, ROUNDING_POLICY);
    }

    @Override
    public void record(Trade trade) {
        tradeStorage.record(trade);
    }

    @Override
    public BigDecimal calculateVolumeWeightedStockPrice() {
        List<Trade> trades = tradeStorage.getValidTrades();
        if (trades.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal counter = trades.stream()
                .map(t -> t.getPrice().multiply(BigDecimal.valueOf(t.getQuantity())))
                .reduce(BigDecimal::add).get();
        long denominator = trades.stream()
                .map(Trade::getQuantity)
                .reduce((a, b) -> a + b)
                .get();

        return counter.divide(BigDecimal.valueOf(denominator), PRECISION, ROUNDING_POLICY);
    }
}
