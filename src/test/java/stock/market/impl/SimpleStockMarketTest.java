package stock.market.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import stock.market.NotRecognizedStockException;
import stock.market.Site;
import stock.market.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleStockMarketTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private Function<List<BigDecimal>, BigDecimal> geometricMeanFunction;
    @Mock
    private StockOperations teaOperations;

    private SimpleStockMarket stockMarket;

    @Before
    public void setUp() throws Exception {
        Map<String, StockOperations> stockSymbolToOperationDelegator = new HashMap<>();
        stockSymbolToOperationDelegator.put("TEA", teaOperations);
        stockMarket = new SimpleStockMarket(stockSymbolToOperationDelegator, geometricMeanFunction);
    }

    @Test
    public void shouldDelegate_CalculateDividedYield() throws Exception {
        //given
        BigDecimal price = new BigDecimal("7");
        BigDecimal expectedResult = new BigDecimal("3.31");
        when(teaOperations.calculateDividedYield(price)).thenReturn(expectedResult);

        //when
        BigDecimal result = stockMarket.calculateDividedYield("TEA", price);

        //then
        assertThat(result).isEqualTo(expectedResult);
        verify(teaOperations, times(1)).calculateDividedYield(any(BigDecimal.class));
        verifyNoMoreInteractions(teaOperations);
    }

    @Test
    public void shouldDelegate_CalculatePERatio() throws Exception {
        //given
        BigDecimal price = new BigDecimal("8");
        BigDecimal expectedResult = new BigDecimal("3.32");
        when(teaOperations.calculatePERatio(price)).thenReturn(expectedResult);

        //when
        BigDecimal result = stockMarket.calculatePERatio("TEA", price);

        //then
        assertThat(result).isEqualTo(expectedResult);
        verify(teaOperations, times(1)).calculatePERatio(any(BigDecimal.class));
        verifyNoMoreInteractions(teaOperations);
    }

    @Test
    public void shouldDelegate_Record() throws Exception {
        //given
        Trade trade = new Trade("TEA", LocalDateTime.now(),1L, Site.Buy,BigDecimal.TEN);

        //when
        stockMarket.record(trade);

        //then
        ArgumentCaptor<Trade> argumentCaptor = ArgumentCaptor.forClass(Trade.class);
        verify(teaOperations).record(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isSameAs(trade);
    }

    @Test
    public void shouldDelegate_CalculateVolumeWeightedStockPrice() throws Exception {
        //given
        BigDecimal expectedResult = new BigDecimal("3.33");
        when(teaOperations.calculateVolumeWeightedStockPrice()).thenReturn(expectedResult);

        //when
        BigDecimal result = stockMarket.calculateVolumeWeightedStockPrice("TEA");

        //then
        assertThat(result).isEqualTo(expectedResult);
        verify(teaOperations, times(1)).calculateVolumeWeightedStockPrice();
        verifyNoMoreInteractions(teaOperations);
    }

    @Test
    public void shouldDelegate_CalculateGBCE() throws Exception {
        BigDecimal expectedResult = new BigDecimal("3.34");
        BigDecimal stockPrice = new BigDecimal("11");
        when(teaOperations.calculateVolumeWeightedStockPrice()).thenReturn(stockPrice);

        when(geometricMeanFunction.apply(any())).thenReturn(expectedResult);

        //when
        BigDecimal result = stockMarket.calculateGBCE();


        //then
        assertThat(result).isEqualTo(expectedResult);
        verify(teaOperations, times(1)).calculateVolumeWeightedStockPrice();
        verifyNoMoreInteractions(teaOperations);
    }

    @Test
    public void shouldThrowExceptionIfStockSymbolNotFound() {
        expectedEx.expect(NotRecognizedStockException.class);
        expectedEx.expectMessage("IBM");

        stockMarket.calculateVolumeWeightedStockPrice("IBM");
    }

    @Test
    public void shouldValidateEmptyPrice() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("price cannot be empty");

        stockMarket.calculateDividedYield("TEA", null);
    }

    @Test
    public void shouldValidateNegativePrice() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("price have to be > 0");

        stockMarket.calculateDividedYield("TEA", new BigDecimal("-1"));
    }
}