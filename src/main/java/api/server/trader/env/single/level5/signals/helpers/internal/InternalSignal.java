//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.internal;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level5.signals.*;

//-------------------------------------------------------------------------------------
public class InternalSignal extends Signal {
//	private String repositoryName;
	private int eventCounter;

//-------------------------------------------------------------------------------------
    public InternalSignal(Debug _debug, Resolver _resolver, SignalConfiguration _configuration, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _configuration, _currencyPair, _args);
        eventCounter = 0;
    	//resolver.getStrategies();
		debug.outln(Debug.WARNING, "InternalSignal._args: "+args);
//    	for (int i = 0; i < _args.size(); i++) {
		Map<String, String> _strategyData = args;
    	resolver.getStrategies()
    			.getStrategy(currencyPair, _strategyData)
    			.getStrategyEvent()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_strategyEvent -> onNext(_strategyEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
    			
//    	_strategy.register(Event.STRATEGY_ENTER_EVENT, this);    		
//    	_strategy.register(Event.STRATEGY_EXIT_EVENT, this);    		

    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onNext(StrategyEvent _strategyEvent) {
//needs debounce!!!
		if(eventCounter%50 == 0){
			debug.outln(Debug.WARNING, "Signal.onNext: "+_strategyEvent);
		}

		eventCounter++;
		
//		float _confidence = _strategyEvent.getConfidence() * confidence;
		float _confidence = confidence;

		int _index = _strategyEvent.getIndex();
		switch(_strategyEvent.getType()) {
			case StrategyEvent.STRATEGY_ENTER: {
//				float _confidence = 1.0;
//				SignalEvent _signalSource = new SignalEvent(SignalEvent.SIGNAL_INTERNAL, _confidence, _event.getObject());
//				Event _newEvent = new Event(Event.SIGNAL_BUY, null, _signalSource, _index);
				SignalEvent _newEvent = new SignalEvent(SignalEvent.SIGNAL_BUY, _confidence, SignalEvent.SOURCE_INTERNAL, _strategyEvent.getStrategy(), _strategyEvent.getBean());
//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
				if(emitter != null){
					emitter.onNext(_newEvent);
				}
				break;
			}
			case StrategyEvent.STRATEGY_EXIT: {
//				float _confidence = 1.0;
//				SignalEvent _signalSource = new SignalEvent(SignalEvent.SIGNAL_INTERNAL, _confidence, _event.getObject());
//				Event _newEvent = new Event(Event.SIGNAL_SELL, null, _signalSource, _index);
				SignalEvent _newEvent = new SignalEvent(SignalEvent.SIGNAL_SELL, _confidence, SignalEvent.SOURCE_INTERNAL, _strategyEvent.getStrategy(), _strategyEvent.getBean());
//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
				if(emitter != null){
					emitter.onNext(_newEvent);
				}
				break;
			}
			default: {
				debug.outln(Debug.WARNING, "Unknown _event int Signal: "+_strategyEvent.getType());
				break;
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }


//-------------------------------------------------------------------------------------
    public void save(){
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "InternalSignal:" + 
		currencyPair + 
		" , " + 
		args;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
