package stock.market;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:beans.xml"})
public class StockMarketErrorHandlingIT {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Autowired
    private StockMarket stockMarket;

    @Test
    public void shouldThrowExceptionIfStockSymbolNotFound() {
        expectedEx.expect(NotRecognizedStockException.class);
        expectedEx.expectMessage("EEE");

        stockMarket.calculatePERatio("EEE", TEN);
    }

    @Test
    public void shouldThrowExceptionIfPriceIsNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("price cannot be empty");

        stockMarket.calculatePERatio("GIN", null);
    }

    @Test
    public void shouldThrowExceptionIfStockPriceNegative() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("price have to be > 0");

        stockMarket.calculatePERatio("GIN", new BigDecimal("-1"));
    }

    @Test
    public void shouldThrowExceptionIfStockPriceZero() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("price have to be > 0");

        stockMarket.calculateDividedYield("GIN", ZERO);
    }

    @Test
    public void shouldThrowExceptionIfTradeNotValid() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Trade timestamp is not set");
        stockMarket.record(new Trade("GIN", null, 1L, Site.Buy, ONE));
    }
}
