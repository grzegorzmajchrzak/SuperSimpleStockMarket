package stock.market;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:beans.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StockMarketMainFunctionalityIT {
    @Autowired
    private StockMarket stockMarket;

    @Test
    public void shouldCalculatePERatioForCommonType() {
        //given
        //when
        BigDecimal result = stockMarket.calculatePERatio("POP", BigDecimal.TEN);
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("12.5"));
    }

    @Test
    public void shouldCalculatePERatioForPreferredType() {
        //given
        //when
        BigDecimal result = stockMarket.calculatePERatio("GIN", BigDecimal.TEN);
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("50"));
    }

    @Test
    public void shouldCalculateDividedYieldForPreferredType() {
        //given
        //when
        BigDecimal result = stockMarket.calculateDividedYield("GIN", BigDecimal.TEN);
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.2"));
    }

    @Test
    public void shouldCalculateVolumeWeightedStockPrice() throws Exception {
        //given
        LocalDateTime old = LocalDateTime.now().minusMinutes(6);
        stockMarket.record(new Trade("TEA", old, 1L, Site.Buy,new BigDecimal("1000")));

        stockMarket.record(new Trade("TEA", LocalDateTime.now(), 1L, Site.Buy,new BigDecimal("20")));
        stockMarket.record(new Trade("TEA", LocalDateTime.now(), 4L, Site.Buy,new BigDecimal("10")));
        //when
        BigDecimal result = stockMarket.calculateVolumeWeightedStockPrice("TEA");
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("12"));
    }

    @Test
    public void shouldCalculateGBCE() throws Exception {
        //given
        LocalDateTime old = LocalDateTime.now().minusMinutes(6);
        stockMarket.record(new Trade("TEA", old, 1L, Site.Buy,new BigDecimal("1000")));

        stockMarket.record(new Trade("TEA", LocalDateTime.now(), 2L, Site.Buy,new BigDecimal("2")));
        stockMarket.record(new Trade("POP", LocalDateTime.now(), 1L, Site.Buy,new BigDecimal("12.5")));
        //when
        BigDecimal result = stockMarket.calculateGBCE();
        //then
        assertThat(result).isEqualByComparingTo(new BigDecimal("5"));
    }
}
