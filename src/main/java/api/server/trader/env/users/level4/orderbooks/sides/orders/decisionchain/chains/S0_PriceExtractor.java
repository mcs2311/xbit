//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;
//import codex.xbit.api.server.trader.env.single.level4.marketmodels.helpers.layer1.marketwisdom.*;


//import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains.*;

//-------------------------------------------------------------------------------------
public class S0_PriceExtractor {
	protected Debug debug;
	protected MarketModel marketModel;

    protected float currentPrice;
    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public S0_PriceExtractor(Debug _debug, MarketModel _marketModel) {
    	debug = _debug;
    	marketModel = _marketModel;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public float extract(Event _event) {
    	
    	if(_event instanceof MarketModelEvent){
    		_event = ((MarketModelEvent)_event).getBeanEvent();
    	} else if(_event instanceof SignalEvent){
    		_event = ((SignalEvent)_event).getBeanEvent();
    	}

    	if(_event instanceof TradeEvent){
    		TradeEvent _tradeEvent = (TradeEvent)_event;
    		currentPrice = _tradeEvent.getPrice().floatValue();
    	} else if(_event instanceof TickerEvent){
    		TickerEvent _tickerEvent = (TickerEvent)_event;
    		currentPrice = _tickerEvent.getAskPrice().floatValue();
    	} else {
		    currentPrice = marketModel.getLastAskPrice().floatValue();	    	
//    		debug.outln(Debug.ERROR, "Error 1201: Unknown type of event in AskPriceManager" + _event);
    	}
    	return currentPrice;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
