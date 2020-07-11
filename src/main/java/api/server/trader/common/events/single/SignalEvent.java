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
public class SignalEvent extends AbstractEvent {
	private int type;
	private int source;
	private Map<String, String> strategy;
	private BeanEvent beanEvent;

	public static final int SIGNAL_BUY = 0;
	public static final int SIGNAL_SELL = 1;

	public static final int SOURCE_INTERNAL = 1000;
	public static final int SOURCE_WHALEALERT = 1001;
	public static final int SOURCE_MANUAL = 1002;
	public static final int SOURCE_DEPTHCHART = 1003;


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public SignalEvent(int _type) {
    	this(_type, (float)0.0, -1, null);
    }

//-------------------------------------------------------------------------------------
    public SignalEvent(int _type, float _confidence) {
    	this(_type, _confidence, -1, null);
    }

//-------------------------------------------------------------------------------------
    public SignalEvent(int _type, float _confidence, BeanEvent _beanEvent) {
    	this(_type, _confidence, -1, _beanEvent);
    }

//-------------------------------------------------------------------------------------
    public SignalEvent(int _type, float _confidence, int _source) {
    	this(_type, _confidence, _source, null);
    }

//-------------------------------------------------------------------------------------
    public SignalEvent(int _type, float _confidence, int _source, BeanEvent _beanEvent) {
		this(_type, _confidence, _source, null, _beanEvent);
    }

//-------------------------------------------------------------------------------------
    public SignalEvent(int _type, float _confidence, int _source, Map<String, String> _strategy, BeanEvent _beanEvent) {
    	super(Event.KIND_SIGNAL, _confidence);
    	type = _type;
    	source = _source;
    	strategy = _strategy;
    	beanEvent = _beanEvent;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public int getSource() {
        return source;
    }

//-------------------------------------------------------------------------------------
    public Map<String, String> getStrategy() {
        return strategy;
    }
    
//-------------------------------------------------------------------------------------
    public BeanEvent getBeanEvent() {
        return beanEvent;
    }

//-------------------------------------------------------------------------------------
   public boolean equals(SignalEvent _sign) {
        return super.equals(_sign) &&
        		(type == _sign.type) &&
        		(source == _sign.source) && 
        		beanEvent.equals(_sign.beanEvent);
   }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	switch(type){
			case SIGNAL_BUY						: return "SIGNAL_BUY";	
			case SIGNAL_SELL					: return "SIGNAL_SELL";	
			default 							: return "UNKNOWN("+type+")";
    	}
    }

//-------------------------------------------------------------------------------------
    public String getSourceName() {
    	switch(source){
			case SOURCE_INTERNAL				: return "SOURCE_INTERNAL";	
			case SOURCE_WHALEALERT				: return "SOURCE_WHALEALERT";	
			case SOURCE_MANUAL					: return "SOURCE_MANUAL";
			case SOURCE_DEPTHCHART				: return "SOURCE_DEPTHCHART";
			default 							: return "UNKNOWN("+source+")";
    	}
    }

//-------------------------------------------------------------------------------------
    public String getStrategyName() {
    	if(strategy == null){
    		return "";
    	}
    	return strategy.get("name");
    }

//-------------------------------------------------------------------------------------
    public long getTime() {
    	if(beanEvent != null){
    		return beanEvent.getTimestamp();
    	} else {
    		return System.currentTimeMillis();
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return super.toString() + "," + getTypeAsString() +", " + getSourceName() +", " + getStrategyName();
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
