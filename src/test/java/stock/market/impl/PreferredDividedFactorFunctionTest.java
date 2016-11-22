package stock.market.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PreferredDividedFactorFunctionTest {

    private PreferredDividedFactorFunction function;

    @Mock
    private StockData stockData;

    @Before
    public void setUp() throws Exception {
        function = new PreferredDividedFactorFunction();
    }

    @Test
    public void shouldMultiptyFixedDividendAndParValue() throws Exception {
        //given
        when(stockData.getFixedDividend()).thenReturn(new BigDecimal("0.01"));
        when(stockData.getParValue()).thenReturn(new BigDecimal("45"));
        //when
        BigDecimal result = function.apply(stockData);
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.45"));
    }
}