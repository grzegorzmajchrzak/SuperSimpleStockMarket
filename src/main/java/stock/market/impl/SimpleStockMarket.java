package stock.market.impl;

import stock.market.NotRecognizedStockException;
import stock.market.StockMarket;
import stock.market.Trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facade between calling application and guts
 * Created by g on 11/16/2016.
 */
public class SimpleStockMarket implements StockMarket {

    private final Map<String, StockOperations> stockSymbolToOperationDelegator;
    private final Function<List<BigDecimal>, BigDecimal> geometricMeanFunction;

    public SimpleStockMarket(Map<String, StockOperations> stockSymbolToOperationDelegator, Function<List<BigDecimal>, BigDecimal> geometricMeanFunction) {
        this.stockSymbolToOperationDelegator = stockSymbolToOperationDelegator;
        this.geometricMeanFunction = geometricMeanFunction;
    }

    private StockOperations getDelegator(String stockSymbol) {
        return Optional.ofNullable(stockSymbolToOperationDelegator.get(stockSymbol)).orElseThrow(() -> new NotRecognizedStockException(stockSymbol));
    }

    private BigDecimal requireValidPrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("price cannot be empty");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price have to be > 0");
        }
        return price;
    }

    @Override
    public BigDecimal calculateDividedYield(String stockSymbol, BigDecimal price) {

        return getDelegator(stockSymbol).calculateDividedYield(requireValidPrice(price));
    }

    @Override
    public BigDecimal calculatePERatio(String stockSymbol, BigDecimal price) {
        return getDelegator(stockSymbol).calculatePERatio(requireValidPrice(price));
    }

    @Override
    public void record(Trade trade) {
        getDelegator(trade.getStockSymbol()).record(trade);
    }

    @Override
    public BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol) {
        return getDelegator(stockSymbol).calculateVolumeWeightedStockPrice();
    }

    @Override
    public BigDecimal calculateGBCE() {
        List<BigDecimal> stockPrices = stockSymbolToOperationDelegator.values().stream()
                .map(StockOperations::calculateVolumeWeightedStockPrice)
                .filter(v -> v.compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
        return geometricMeanFunction.apply(stockPrices);
    }
}
