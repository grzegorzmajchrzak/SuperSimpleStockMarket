package stock.market.impl;

import stock.market.Trade;

import java.util.List;

public interface TradeStorage {
    void record(Trade trade);
    List<Trade> getValidTrades();
}
