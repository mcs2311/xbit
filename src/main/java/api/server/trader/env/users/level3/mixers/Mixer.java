//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level3.mixers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import io.reactivex.*;
import io.reactivex.disposables.*;
import io.reactivex.schedulers.*;

import org.knowm.xchange.currency.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;


import codex.xbit.api.server.trader.env.users.level2.shadoworders.*;
import codex.xbit.api.server.trader.env.users.level3.agents.*;


//-------------------------------------------------------------------------------------
public class Mixer extends AbstractEntity<Configuration> implements FlowableOnSubscribe<UserTradeEvent> {
	private String exchange;
	private CurrencyPair currencyPair;


	private Agents agents;
	private ShadowOrders shadowOrders;
//	private LinkedTransferQueue<Event> queue;
	
    //---cache:

	//---statics:

    //---rx:
	private Flowable<UserTradeEvent> flowable;
	private FlowableEmitter<UserTradeEvent> emitter;

//-------------------------------------------------------------------------------------
    public Mixer(Debug _debug, Resolver _resolver, String _exchange, CurrencyPair _currencyPair) {
    	super(_debug, _resolver);
    	exchange = _exchange;
    	currencyPair = _currencyPair;

    	agents = resolver.getAgents();
    	shadowOrders = resolver.getShadowOrders();
//		queue = new LinkedTransferQueue<Event>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);
    	load();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(UserTradeEvent _userTradeEvent){
//    	queue.put(_userTradeEvent);
    	debug.outln(Debug.IMPORTANT3, "Mixer.onNext.0:" + _userTradeEvent + ", trade="+_userTradeEvent.getTrade());
    	if(emitter != null){
    		debug.outln(Debug.IMPORTANT3, "Mixer.onNext.1.." );
    		if(!shadowOrders.matchOrders(_userTradeEvent)){
    			debug.outln(Debug.IMPORTANT3, "Mixer.onNext.2.." );
    			emitter.onNext(_userTradeEvent);
    		}
    	}
    }

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserTradeEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserTradeEvent> getRawTrade(long _highestTradeId, long _latestTradeTime) {
    		resolver.getRawTrades()
    			.getRawTradesByExchangeAndCurrencyPair(exchange, currencyPair)
    			.getRawTrade(_highestTradeId, _latestTradeTime)
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_userTradeEvent -> onNext(_userTradeEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized void load() {
    	super.load();
	}

//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln(Debug.IMPORTANT3, "Agent save...");
    	super.save();
    }


//-------------------------------------------------------------------------------------
    public String toString() {
		return "Mixer:" + exchange + ", " + currencyPair;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------