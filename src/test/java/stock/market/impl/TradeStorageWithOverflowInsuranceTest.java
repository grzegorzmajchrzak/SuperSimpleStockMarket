package stock.market.impl;

import org.fest.assertions.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import stock.market.Site;
import stock.market.Trade;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

public class TradeStorageWithOverflowInsuranceTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private TradeStorageWithOverflowInsurance storage = new TradeStorageWithOverflowInsurance(Duration.ofMinutes(1));

    @Test
    public void shouldWorkProperlyWithDefaultSettings() throws Exception {
        //given
        Trade trade1 = new Trade("IBM", now(), 1L, Site.Sell, BigDecimal.ONE);
        Trade trade2 = new Trade("IBM", now().minus(1, SECONDS), 1L, Site.Buy, BigDecimal.ONE);
        Trade tradeOutdated = new Trade("IBM", now().minus(3, MINUTES), 1L, Site.Sell, BigDecimal.ONE);
        storage.record(trade1);
        storage.record(tradeOutdated);
        storage.setOperationSinceLastRemovingOldTrades(100000);
        storage.record(trade2);
        //when
        List<Trade> trades = storage.getValidTrades();
        //then
        Assertions.assertThat(trades).containsExactly(trade1, trade2);
    }

    @Test
    public void shouldValidateTradePriceNotNull() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Trade price is not set");

        storage.record(new Trade("IBM",now(), 1L, Site.Sell, null));
    }

    @Test
    public void shouldValidateTradePriceNotPositive() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Trade price have to be > 0");

        storage.record(new Trade("IBM",now(), 1L, Site.Sell, BigDecimal.ZERO));
    }

    @Test
    public void shouldValidateTradeQuantityNotSet() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Trade quantity is not set");

        storage.record(new Trade("IBM",now(), null, Site.Sell, BigDecimal.TEN));
    }

    @Test
    public void shouldValidateTradeQuantityNotPositive() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Trade quantity have to be > 0");

        storage.record(new Trade("IBM",now(), -2L, Site.Sell, BigDecimal.TEN));
    }

    @Test
    public void shouldValidateTradeTimestampNotSet() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Trade timestamp is not set");

        storage.record(new Trade("IBM", null, 2L, Site.Sell, BigDecimal.TEN));
    }
}