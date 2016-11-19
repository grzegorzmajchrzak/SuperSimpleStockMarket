package stock.market;

public class NotRecognizedStockException extends RuntimeException {
    public NotRecognizedStockException(String stockSymbol){
        super(stockSymbol);
    }
}
