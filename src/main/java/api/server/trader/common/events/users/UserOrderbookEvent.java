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
import codex.xbit.api.server.trader.core.components.orders.*;

//-------------------------------------------------------------------------------------
public class UserOrderbookEvent extends AbstractEvent {
	private int type;
	private AbstractOrder order;

	public static final int ORDER_ADDED 	= 0;
	public static final int ORDER_REMOVED 	= 1;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserOrderbookEvent(int _type) {
    	this(_type, null);
    }

//-------------------------------------------------------------------------------------
    public UserOrderbookEvent(int _type, AbstractOrder _order) {
    	super(Event.KIND_USER_ORDERBOOK);
    	type = _type;
    	order = _order;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }
    
//-------------------------------------------------------------------------------------
    public AbstractOrder getOrder() {
        return order;
    }


//-------------------------------------------------------------------------------------
   public boolean equals(UserOrderbookEvent _userOrderbookEvent) {
        return (type == _userOrderbookEvent.type) &&
        		order.equals(_userOrderbookEvent.order);
   }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return null;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
