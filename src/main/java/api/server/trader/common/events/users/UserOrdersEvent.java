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
//import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;


//-------------------------------------------------------------------------------------
public class UserOrdersEvent extends AbstractEvent {
	private int type;
	private AbstractOrder order;
	private int numberOfOpenDeals;
	private boolean isALockSwitch;

	public static final int ORDERS_UPDATE = 0;
//	public static final int ORDERS_DETECTED_NEW = 1;
//	public static final int ORDERS_NUMBER_OF_OPEN_DEALS_CHANGED = 2;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserOrdersEvent(int _type) {
    	this(_type, null, 0, false);
    }


//-------------------------------------------------------------------------------------
    public UserOrdersEvent(int _type, AbstractOrder _order) {
    	this(_type, _order, 0, false);
    }

//-------------------------------------------------------------------------------------
    public UserOrdersEvent(int _type, int _numberOfOpenDeals) {
    	this(_type, null, _numberOfOpenDeals, false);
    }

//-------------------------------------------------------------------------------------
    public UserOrdersEvent(int _type, int _numberOfOpenDeals, boolean _isALockSwitch) {
    	this(_type, null, _numberOfOpenDeals, _isALockSwitch);
    }

//-------------------------------------------------------------------------------------
    public UserOrdersEvent(int _type, AbstractOrder _order, int _numberOfOpenDeals, boolean _isALockSwitch) {
    	super(Event.KIND_USER_ORDERS);
    	type = _type;
    	order = _order;    	
    	numberOfOpenDeals = _numberOfOpenDeals;
    	isALockSwitch = _isALockSwitch;
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
    public int getNumberOfOpenDeals() {
        return numberOfOpenDeals;
    }

//-------------------------------------------------------------------------------------
   public boolean equals(UserOrdersEvent _userOrdersEvent) {
        return (type == _userOrdersEvent.type);
   }

//-------------------------------------------------------------------------------------
   public boolean isALockSwitch(){
   		return isALockSwitch;
   }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	switch(type){
			case ORDERS_UPDATE								: return "ORDERS_UPDATE";	
//			case ORDERS_DETECTED_NEW						: return "ORDERS_DETECTED_NEW";	
//			case ORDERS_NUMBER_OF_OPEN_DEALS_CHANGED		: return "ORDERS_NUMBER_OF_OPEN_DEALS_CHANGED";	
//			case ORDERBOOK_UNKNOWN_ORDER					: return "ORDERBOOK_UNKNOWN_ORDER";	
			default 										: return "UNKNOWN("+type+")";
    	}
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return getTypeAsString() + ", numberOfOpenDeals="+numberOfOpenDeals+", isALockSwitch="+isALockSwitch;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
