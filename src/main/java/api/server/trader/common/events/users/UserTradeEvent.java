//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events.users;
//-------------------------------------------------------------------------------------
//import java.time.*;
//import java.math.*;

//import org.knowm.xchange.currency.CurrencyPair;
//import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;


//-------------------------------------------------------------------------------------
public class UserTradeEvent extends AbstractEvent {
	private int type;
	private AbstractTrade trade;

	public static final int TRADE_UPDATE = 0;
	public static final int TRADE_DETECTED_NEW = 1;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserTradeEvent(int _type) {
    	this(_type, null);
    }


//-------------------------------------------------------------------------------------
    public UserTradeEvent(int _type, AbstractTrade _trade) {
    	super(Event.KIND_USER_TRADE);
    	type = _type;
    	trade = _trade;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }
  
    
//-------------------------------------------------------------------------------------
    public AbstractTrade getTrade() {
        return trade;
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	switch(type){
			case TRADE_UPDATE						: return "TRADE_UPDATE";	
			case TRADE_DETECTED_NEW					: return "TRADE_DETECTED_NEW";	
			default 							: return "UNKNOWN("+type+")";
    	}
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return "UserTradeEvent:" + getTypeAsString() + ":" + trade;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
