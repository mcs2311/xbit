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
public class UserOrderEvent extends AbstractEvent {
	private int type;
	private int previousState;
	private int currentState;
	private AbstractOrder order;

	public static final int ORDER_UPDATE = 0;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserOrderEvent(int _type) {
    	this(_type, StateOrder.STATE_NONE, StateOrder.STATE_NONE, null);
    }

//-------------------------------------------------------------------------------------
    public UserOrderEvent(int _type, AbstractOrder _order) {
    	this(_type, StateOrder.STATE_NONE, StateOrder.STATE_NONE, _order);
    }

//-------------------------------------------------------------------------------------
    public UserOrderEvent(int _type, int _previousState, int _currentState) {
    	this(_type, _previousState, _currentState, null);
    }

//-------------------------------------------------------------------------------------
    public UserOrderEvent(int _type, int _previousState, int _currentState, AbstractOrder _order) {
    	super(Event.KIND_USER_ORDER);
    	type = _type;
    	previousState = _previousState;
    	currentState = _currentState;
    	order = _order;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	switch(type){
			case ORDER_UPDATE								: return "ORDER_UPDATE";	
			default 										: return "UNKNOWN("+type+")";
    	}
    }    
//-------------------------------------------------------------------------------------
    public AbstractOrder getOrder() {
        return order;
    }

//-------------------------------------------------------------------------------------
    public boolean isALockSwitch() {
    	return (currentState == StateOrder.STATE_LOCKED);
//    	return (((previousState == StateOrder.STATE_EXECUTED) && (currentState == StateOrder.STATE_LOCKED)) ||
//    		((previousState == StateOrder.STATE_LOCKED) && (currentState == StateOrder.STATE_EXECUTED)));
    }

//-------------------------------------------------------------------------------------
   public boolean equals(UserOrderEvent _userOrderEvent) {
        return (type == _userOrderEvent.type) &&
        		order.equals(_userOrderEvent.order);
   }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return getTypeAsString() + ", previousState=" + previousState + ", currentState=" + currentState;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
