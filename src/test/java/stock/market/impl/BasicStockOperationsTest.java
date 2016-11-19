package stock.market.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import stock.market.Site;
import stock.market.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Function;

import static java.math.BigDecimal.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BasicStockOperationsTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private static final BigDecimal DIVIDED_YIELD_RESULT = TEN;
    @Mock
    private Function<StockData, BigDecimal> dividedYieldProvider;
    @Mock
    private StockData stockData;
    @Mock
    private TradeStorage tradeStorage;
    private BasicStockOperations operations;

    @Before
    public void setUp() throws Exception {
        operations = new BasicStockOperations(dividedYieldProvider, stockData, tradeStorage);
        when(dividedYieldProvider.apply(any())).thenReturn(DIVIDED_YIELD_RESULT);
    }

    @Test
    public void shouldValidateConstructorDividedYieldProvider() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("dividedYieldProvider cannot be null");

        new BasicStockOperations(null, stockData, tradeStorage);
    }

    @Test
    public void shouldValidateConstructorStockData() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("stockData cannot be null");

        new BasicStockOperations(dividedYieldProvider, null, tradeStorage);
    }

    @Test
    public void shouldValidateConstructorTradeStorage() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("tradeStorage cannot be null");

        new BasicStockOperations(dividedYieldProvider, stockData, null);
    }

    @Test
    public void shouldDelegateCalculateDividedYield() {
        //given
        //when
        BigDecimal result = operations.calculateDividedYield(ONE);
        //then
        assertThat(result).isEqualByComparingTo(DIVIDED_YIELD_RESULT);
        verify(dividedYieldProvider, times(1)).apply(stockData);
    }


    @Test
    public void shouldCalculatePERatio() {
        //given
        //when
        BigDecimal result = operations.calculatePERatio(ONE);
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.1"));
    }

    @Test
    public void shouldCalculatePERatioEvenIfDividendIsZero() {
        //given
        when(dividedYieldProvider.apply(any())).thenReturn(ZERO);
        //when
        BigDecimal result = operations.calculatePERatio(ONE);
        //then
        assertThat(result).isEqualByComparingTo(ZERO);

    }

    @Test
    public void shouldDelegateRecord() {
        //given
        Trade trade = mock(Trade.class);
        //when
        operations.record(trade);
        //then
        verify(tradeStorage, times(1)).record(trade);
    }

    @Test
    public void shouldCalculateVolumeWeightedStockPrice() {
        //given
        Trade trade1 = new Trade("IBM", LocalDateTime.now(), 2L, Site.Sell, ONE);
        Trade trade2 = new Trade("IBM", LocalDateTime.now().minus(1, SECONDS), 1L, Site.Buy, TEN);
        when(tradeStorage.getValidTrades()).thenReturn(asList(trade1, trade2));

        //when
        BigDecimal result = operations.calculateVolumeWeightedStockPrice();
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("4"));
    }

    @Test
    public void shouldCalculateVolumeWeightedStockPriceSupportNoTradesCase() {
        //given
        when(tradeStorage.getValidTrades()).thenReturn(Collections.emptyList());

        //when
        BigDecimal result = operations.calculateVolumeWeightedStockPrice();
        //then
        assertThat(result).isEqualByComparingTo(ZERO);
    }

}