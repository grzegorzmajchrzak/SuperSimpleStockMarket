Assignment - Super Simple Stock Market
======
I've decided to create and expose required functionality as a java component with API extracted to java interface. Internal implementation is hidden behind *facade*.

Assumptions
======
  *  It is not obvious what dot mean in task description formula for "Dividend Yield" for "Preferred Type". I assumed it means multiplication. So formula is: (Fixed Dividend)*(Par Value)/Price
  *  To calculate "GBCE All Share Index" only non-zero "Volume Weighted Stock Prices" for "Stock Symbols" are taken into considerations
  *  If calculated "Dividend Yield" is zero then "P/E Ratio" is also zero.

Decisions
======
  *  Use to calculations BigDecimal type with ROUND_HALF_UP as rounding policy and 16 digits precision (see BasicStockOperations.java). Exception from this rule is geometric mean calculation. There, because of complexity and speed operations are made in Double type. The cost here is more limited precision.
  *  Validations for data send by api (StockMarket.java). I have implemented basic validation for fields that are used by SuperSimpleStockMarket.
  *  Retention time for trades to prevent OutOfMemoryError
  *  Spring XML configuration with only main settings
  *  Module is not thread-safe
  *  No big upfront design, not too much extension points.
  *  Tests works as a documentation. Integration tests to show how to use module.


