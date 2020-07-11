//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events.single;
//-------------------------------------------------------------------------------------
//import java.time.*;
//import java.math.*;

//import org.knowm.xchange.currency.CurrencyPair;
//import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

//-------------------------------------------------------------------------------------
public class MarketModelEvent extends AbstractEvent {
	private int direction;
	private BeanEvent beanEvent;

	public static final int MARKET_GOING_UP 	= 1;
	public static final int MARKET_STALL 		= 2;
	public static final int MARKET_GOING_DOWN 	= 3;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public MarketModelEvent(int _direction) {
    	this(_direction, null, (float)0.0);
    }

//-------------------------------------------------------------------------------------
    public MarketModelEvent(int _direction, BeanEvent _beanEvent, float _confidence) {
    	super(Event.KIND_MARKETMODEL, _confidence);
    	direction = _direction;
    	beanEvent = _beanEvent;
    }

//-------------------------------------------------------------------------------------
    public int getDirection() {
        return direction;
    }

//-------------------------------------------------------------------------------------
    public String getDirectionAsString() {
        switch(direction){
        	case MARKET_GOING_UP: 	return "GOING_UP";
        	case MARKET_STALL: 		return "STALL";
        	case MARKET_GOING_DOWN: return "GOING_DOWN";
        	default: 				return "UNKNOWN";
        }
    }

//-------------------------------------------------------------------------------------
    public BeanEvent getBeanEvent() {
        return beanEvent;
    }

//-------------------------------------------------------------------------------------
    public long getTime() {
    	if(beanEvent != null){
    		return beanEvent.getTimestamp();
    	} else {
    		return System.currentTimeMillis();
    	}
    }

/*
//-------------------------------------------------------------------------------------
   public boolean equals(MarketModelEvent _marketModelEvent) {
        return (direction == _marketModelEvent.direction) &&
        		beanEvent.equals(_marketModelEvent.beanEvent);
   }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return "MarketModelEvent: " + getDirectionAsString() + ", " + beanEvent + ", " + super.toString();
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
