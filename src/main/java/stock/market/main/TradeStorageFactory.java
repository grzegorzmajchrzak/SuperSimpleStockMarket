package stock.market.main;

import stock.market.impl.TradeStorage;

public interface TradeStorageFactory {
    TradeStorage create();
}
