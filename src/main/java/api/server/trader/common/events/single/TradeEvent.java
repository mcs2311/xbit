//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events.single;
//-------------------------------------------------------------------------------------
import java.time.*;
import java.time.format.*;
import java.math.*;

//import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

//-------------------------------------------------------------------------------------
public class TradeEvent extends BeanEvent {
	protected Trade trade;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public TradeEvent(Trade _trade) {
    	super(KIND_TRADE);
    	trade = _trade;
    }

//-------------------------------------------------------------------------------------
    public TradeEvent(TradeEvent _tradeEvent) {
    	super(KIND_TRADE);
    	trade = _tradeEvent.trade;
    	index = _tradeEvent.index;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ZonedDateTime getTime() {
        return trade.getTimestamp().toInstant().atZone(ZONE_ID);
    }

//-------------------------------------------------------------------------------------
    public long getTimestamp() {
        return trade.getTimestamp().getTime();
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getPrice() {
        return trade.getPrice();
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getAmount() {
        return trade.getOriginalAmount();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString(){
        return "[trade]: price=" + getPrice() + ", amount=" + getAmount() + ", time=" + getTime().format(formatter0) + super.toString();// + ",@" + hashCode();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
