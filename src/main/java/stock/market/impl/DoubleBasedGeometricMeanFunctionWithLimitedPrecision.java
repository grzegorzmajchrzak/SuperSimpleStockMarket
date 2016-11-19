package stock.market.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * Calculate geometric mean by using <b>doubles</b> to keep calculations fast and simple
 */
public class DoubleBasedGeometricMeanFunctionWithLimitedPrecision implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> arguments) {
        double multiplication = arguments.stream().reduce(BigDecimal.ONE, BigDecimal::multiply).doubleValue();
        double b = 1d / arguments.size();
        return BigDecimal.valueOf(Math.pow(multiplication, b));
    }


}
