package stock.market.impl;

import stock.market.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TradeStorageWithOverflowInsurance implements TradeStorage {

    private final static int MAX_OPERATIONS_BETWEEN_REMOVING_OLD_TRADES = 5000;
    private int operationSinceLastRemovingOldTrades;

    private final List<Trade> trades = new LinkedList<>();
    private final TemporalAmount validTradeRetentionTimeAmount;

    public TradeStorageWithOverflowInsurance(TemporalAmount validTradeRetentionTimeAmount) {
        this.validTradeRetentionTimeAmount = validTradeRetentionTimeAmount;
    }

    @Override
    public void record(Trade trade) {
        validate(trade);
        operationSinceLastRemovingOldTrades++;
        removeOldTradesIfNeeded();
        trades.add(trade);
    }

    private void validate(Trade trade) {
        if (trade.getPrice() == null) {
            throw new IllegalArgumentException("Trade price is not set");
        }
        if (trade.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Trade price have to be > 0");
        }
        if (trade.getQuantity() == null) {
            throw new IllegalArgumentException("Trade quantity is not set");
        }
        if (trade.getQuantity() <= 0) {
            throw new IllegalArgumentException("Trade quantity have to be > 0");
        }
        if (trade.getTimestamp() == null) {
            throw new IllegalArgumentException("Trade timestamp is not set");
        }

    }

    @Override
    public List<Trade> getValidTrades() {
        removeOldTrades();
        return trades;
    }

    private void removeOldTradesIfNeeded() {
        if (shouldTryToRemoveOldTrades()) {
            removeOldTrades();
        }
    }

    private boolean shouldTryToRemoveOldTrades() {
        return operationSinceLastRemovingOldTrades > MAX_OPERATIONS_BETWEEN_REMOVING_OLD_TRADES;
    }

    private void removeOldTrades() {
        LocalDateTime threshold = LocalDateTime.now().minus(validTradeRetentionTimeAmount);
        for (Iterator<Trade> it = trades.iterator(); it.hasNext(); ) {
            if (it.next().getTimestamp().isBefore(threshold)) {
                it.remove();
            }
        }
        operationSinceLastRemovingOldTrades = 0;
    }

    public void setOperationSinceLastRemovingOldTrades(int operationSinceLastRemovingOldTrades) {
        this.operationSinceLastRemovingOldTrades = operationSinceLastRemovingOldTrades;
    }
}
