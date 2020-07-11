//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events.single;
//-------------------------------------------------------------------------------------
import java.util.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

//-------------------------------------------------------------------------------------
public class PilotEvent extends AbstractEvent {
	private int type;

	public static final int PILOT_BEAR_BEGIN 		= 10;
	public static final int PILOT_BEAR_END 			= 11;
	public static final int PILOT_BULL_BEGIN 		= 12;
	public static final int PILOT_BULL_END 			= 13;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public PilotEvent(int _type, float _confidence) {
    	super(Event.KIND_PILOT, _confidence);
    	type = _type;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public boolean isABuySignal() {
        return ((type == PILOT_BEAR_END) || (type == PILOT_BULL_BEGIN));
    }

//-------------------------------------------------------------------------------------
    public boolean isASellSignal() {
        return ((type == PILOT_BEAR_BEGIN) || (type == PILOT_BULL_END));
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	switch(type){
			case PILOT_BEAR_BEGIN			: return "PILOT_BEAR_BEGIN";	
			case PILOT_BEAR_END 			: return "PILOT_BEAR_END";	
			case PILOT_BULL_BEGIN			: return "PILOT_BULL_BEGIN";	
			case PILOT_BULL_END 			: return "PILOT_BULL_END";	
			default 						: return "UNKNOWN("+type+")";
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
    	return super.toString() + "," + getTypeAsString();
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
