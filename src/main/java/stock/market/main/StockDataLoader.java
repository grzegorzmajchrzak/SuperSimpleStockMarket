package stock.market.main;

import stock.market.impl.BasicStockOperations;
import stock.market.impl.StockData;
import stock.market.impl.StockOperations;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Load Stock Data from csv resource
 */
public class StockDataLoader {

    private final URI csvFileLocation;
    private final TradeStorageFactory tradeStorageFactory;

    private final Map<String, Function<StockData, BigDecimal>> typeToDividedFactorFunction;

    public StockDataLoader(URI csvFileLocation, TradeStorageFactory tradeStorageFactory, Map<String, Function<StockData, BigDecimal>> typeToDividedYieldFunction) {
        this.csvFileLocation = csvFileLocation;
        this.tradeStorageFactory = tradeStorageFactory;
        this.typeToDividedFactorFunction = typeToDividedYieldFunction;
    }

    public Map<String, StockOperations> loadData() throws IOException {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(csvFileLocation))) {
                return stream.skip(1).collect(Collectors.toMap(this::getStockSymbol, this::createOperation));
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("cannot load data from source: " + csvFileLocation, e);
        }
    }

    private String getStockSymbol(String stockDefinition) {
        return stockDefinition.split(",")[0];
    }

    private StockOperations createOperation(String stockDefinition) {
        String[] columns = stockDefinition.split(",");
        String type = columns[1];
        BigDecimal lastDividend = toBigDecimal(columns[2]);
        BigDecimal fixedDividend = toBigDecimal(columns[3]);
        BigDecimal parValue = toBigDecimal(columns[4]);
        StockData stockData = new StockData(lastDividend, fixedDividend, parValue);
        return new BasicStockOperations(typeToDividedFactorFunction.get(type), stockData, tradeStorageFactory.create());
    }

    private BigDecimal toBigDecimal(String value) {
        return value.isEmpty() ? null : new BigDecimal(value);
    }
}
