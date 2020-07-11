//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events.single;
//-------------------------------------------------------------------------------------
import java.time.*;
import java.math.*;
import java.text.*;
import java.util.Date;

//import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.orders.*;

//-------------------------------------------------------------------------------------
public class OrderbookEvent extends BeanEvent {
	private OrderBook orderBook;
	private long bidsSize;
	private long asksSize;

	//---statics:
	private final static DecimalFormat df = new DecimalFormat("#.##");

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public OrderbookEvent(OrderBook _orderBook) {
    	this(_orderBook, 0L, 0L);
    }

//-------------------------------------------------------------------------------------
    public OrderbookEvent(OrderBook _orderBook, long _bidsSize, long _asksSize) {
    	super(KIND_ORDERBOOK);
    	orderBook = _orderBook;
    	bidsSize = _bidsSize;
    	asksSize = _asksSize;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public OrderBook getOrderBook() {
        return orderBook;
    }

//-------------------------------------------------------------------------------------
    public ZonedDateTime getTime() {
        return orderBook.getTimeStamp().toInstant().atZone(ZONE_ID);
    }

//-------------------------------------------------------------------------------------
    public long getTimestamp() {
    	if(orderBook == null){
    		return System.currentTimeMillis();
    	}
    	Date _date = orderBook.getTimeStamp();
    	if(_date == null){
    		return System.currentTimeMillis();    		
    	}
        return _date.getTime();
    }

//-------------------------------------------------------------------------------------
    public long getBidsSize() {
        return bidsSize;
    }

//-------------------------------------------------------------------------------------
    public long getAsksSize() {
        return asksSize;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return "[orderbook]: bidsSize=[" + Float.valueOf(df.format((float)bidsSize / 1000000)) + "], asksSize=[" + Float.valueOf(df.format((float)asksSize / 1000000)) + "]" + super.toString();
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
