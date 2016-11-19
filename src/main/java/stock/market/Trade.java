package stock.market;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Trade {
    private String stockSymbol;
    private LocalDateTime timestamp;
    private Long quantity;
    private Site site;
    private BigDecimal price;
}
