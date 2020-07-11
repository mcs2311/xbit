//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;

import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;


//-------------------------------------------------------------------------------------
public class S1_SignalFilterChain {
	protected Debug debug;
	protected Resolver resolver;
	protected int type;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public S1_SignalFilterChain(Debug _debug, Resolver _resolver, int _type) {
    	debug = _debug;
    	resolver = _resolver;
    	type = _type;
    }

//-------------------------------------------------------------------------------------
    public Event filter(Event _event){
    	return _event;
    }
    
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
