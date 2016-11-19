package stock.market.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleBasedGeometricMeanFunctionWithLimitedPrecisionTest {
    private static final double DELTA = 0.01d;
    private DoubleBasedGeometricMeanFunctionWithLimitedPrecision calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new DoubleBasedGeometricMeanFunctionWithLimitedPrecision();
    }

    private void assertCalculationWithDelta(String expectedResult, String arguments) {
        List<BigDecimal> bigDecimals = Arrays.stream(arguments.split(", ")).map(BigDecimal::new).collect(Collectors.toList());
        BigDecimal result = calculator.apply(bigDecimals);
        Assert.assertEquals(Double.valueOf(expectedResult), result.doubleValue(), DELTA);
    }

    @Test
    public void shouldCalculateCorrectValue() throws Exception {
        assertCalculationWithDelta("4", "2, 8");
        assertCalculationWithDelta("3.71", "3.71, 3.71, 1369, 0.01");
    }
}