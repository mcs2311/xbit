//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events.single;
//-------------------------------------------------------------------------------------
import java.util.*;
//import java.math.*;

//import org.knowm.xchange.currency.CurrencyPair;
//import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

//-------------------------------------------------------------------------------------
public class StrategyEvent extends AbstractEvent {
	private int type;
	private BeanEvent bean;
	protected Map<String, String> strategy;

	public static final int STRATEGY_ENTER = 0;
	public static final int STRATEGY_EXIT = 1;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public StrategyEvent(int _type) {
    	this(_type/*, 0f*/, null, null);
    }

//-------------------------------------------------------------------------------------
    public StrategyEvent(int _type/*, int _confidence*/, Map<String, String> _strategy, BeanEvent _beanEvent) {
    	super(Event.KIND_STRATEGY/*, _confidence*/);
    	type = _type;
    	strategy = _strategy;
    	bean = _beanEvent;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }
    
//-------------------------------------------------------------------------------------
    public Map<String, String> getStrategy() {
        return strategy;
    }
    
//-------------------------------------------------------------------------------------
    public BeanEvent getBean() {
        return bean;
    }

//-------------------------------------------------------------------------------------
    public int getIndex() {
        return bean.getIndex();
    }

//-------------------------------------------------------------------------------------
   public boolean equals(StrategyEvent _strategyEvent) {
        return (type == _strategyEvent.type) &&
        		bean.equals(_strategyEvent.bean);
   }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	switch(type){
			case STRATEGY_ENTER						: return "STRATEGY_ENTER";	
			case STRATEGY_EXIT					: return "STRATEGY_EXIT";	
			default 							: return "UNKNOWN("+type+")";
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return getTypeAsString() + ", " + bean;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
