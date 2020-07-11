//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events;
//-------------------------------------------------------------------------------------
//import java.time.*;
//import java.math.*;

//import org.knowm.xchange.currency.CurrencyPair;
//import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class AbstractEvent implements Event {
	private int kind;
	private float confidence;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public AbstractEvent(int _kind) {
    	this(_kind, (float)0.1);
    }

//-------------------------------------------------------------------------------------
    public AbstractEvent(int _kind, float _confidence) {
    	kind = _kind;
    	confidence = _confidence;
    }

//-------------------------------------------------------------------------------------
    public int getKind(){
    	return kind;
    }

//-------------------------------------------------------------------------------------
    public float getConfidence() {
        return confidence;
    }

//-------------------------------------------------------------------------------------
    public void setConfidence(float _confidence) {
        confidence = _confidence;
    }

//-------------------------------------------------------------------------------------
   public String getName() {
        switch(kind){
        	case KIND_TRADE 				: return "TRADE";
        	case KIND_TICKER 				: return "TICKER";
        	case KIND_ORDERBOOK 			: return "ORDERBOOK";
        	case KIND_STRATEGY 				: return "STRATEGY";
        	case KIND_MARKETMODEL 			: return "MARKETMODEL";
        	case KIND_SIGNAL 				: return "SIGNAL";
        	case KIND_PILOT 				: return "PILOT";

        	case KIND_USER_TRADE 			: return "USER_TRADE";
        	case KIND_USER_BALANCE 			: return "USER_BALANCE";
        	case KIND_USER_ORDER 			: return "USER_ORDER";
        	case KIND_USER_ORDERS 			: return "USER_ORDERS";
        	case KIND_USER_ORDERBOOK 		: return "USER_ORDERBOOK";

        	default 						: return "ERROR";
        }
   }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
   public boolean equals(AbstractEvent _event) {
        return (kind == _event.kind) &&
        		(confidence == _event.confidence);
   }

//-------------------------------------------------------------------------------------
    public String toString() {
    	return getName() +", confidence="+confidence;    
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
