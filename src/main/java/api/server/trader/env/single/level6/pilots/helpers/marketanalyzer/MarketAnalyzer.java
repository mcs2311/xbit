//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level6.pilots.helpers.marketanalyzer;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.jline.utils.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;

import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.*;

//-------------------------------------------------------------------------------------
public class MarketAnalyzer extends AbstractItem {
	private AutoPilot autoPilot;
	private ProbabilityAnalyzer bearProbability;
	private ProbabilityAnalyzer bullProbability;

	//---statics:

//-------------------------------------------------------------------------------------
    public MarketAnalyzer(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    	bearProbability = new ProbabilityAnalyzer(_debug, _resolver);
    	bullProbability = new ProbabilityAnalyzer(_debug, _resolver);
    }

//-------------------------------------------------------------------------------------
    public synchronized void setAutoPilot(AutoPilot _autoPilot){
		autoPilot = _autoPilot;
    }

//-------------------------------------------------------------------------------------
    protected void onNextEvent(Event _event){
//    	debug.outln(Debug.ERROR, "Unhandled event in AutoPilot!");
    	computeMarketProbabilities(_event);
    	if(autoPilot != null){
    		autoPilot.takeDecisionOnMarketChanges(_event);
    	}/* else {
    		debug.outln(Debug.ERROR, "null autoPilot in MarketAnalyzer");
    	}*/
    }

//-------------------------------------------------------------------------------------
    protected void computeMarketProbabilities(Event _event){
//    	debug.outln(Debug.INFO, "computeMarketProbabilities:");
    	if(_event instanceof SignalEvent){
    		computeMarketProbabilities((SignalEvent)_event);
    	} else if(_event instanceof MarketModelEvent){
    		computeMarketProbabilities((MarketModelEvent)_event);    		
    	}    	
    }

//-------------------------------------------------------------------------------------
    protected void computeMarketProbabilities(SignalEvent _signalEvent){
//    	debug.outln(Debug.INFO, "computeMarketProbabilities.SignalEvent.in: bearProbability="+bearProbability+", bullProbability="+bullProbability+", _signalEvent="+_signalEvent);
    	int _source = _signalEvent.getSource();
    	long _time = _signalEvent.getTime();
    	if(_source == SignalEvent.SOURCE_WHALEALERT){
    		int _type = _signalEvent.getType();
    		float _confidence = _signalEvent.getConfidence();
    		if(_type == SignalEvent.SIGNAL_SELL){
		    	bearProbability.set(_confidence, _time);
		    	bullProbability.set(ProbabilityAnalyzer.DEFAULT_MIN_PROBABILITY, _time);
    		} else if(_type == SignalEvent.SIGNAL_BUY){
		    	bearProbability.set(ProbabilityAnalyzer.DEFAULT_MIN_PROBABILITY, _time);
		    	bullProbability.set(_confidence, _time);
    		}
    	}
    	debug.outln(Debug.INFO, "computeMarketProbabilities.SignalEvent.out: bearProbability="+bearProbability+", bullProbability="+bullProbability+", _signalEvent="+_signalEvent);
    }

//-------------------------------------------------------------------------------------
    protected void computeMarketProbabilities(MarketModelEvent _marketModelEvent){
    	int _direction = _marketModelEvent.getDirection();
    	float _confidence = _marketModelEvent.getConfidence();
    	if(_confidence < 0.3){
    		return;
    	}
//    	debug.outln(Debug.INFO, "computeMarketProbabilities.MarketModelEvent.in: bearProbability="+bearProbability+", bullProbability="+bullProbability+", _marketModelEvent="+_marketModelEvent);
    	long _time = _marketModelEvent.getTime();
    	if(_direction == MarketModelEvent.MARKET_GOING_UP){
	    	bearProbability.down(_confidence, _time);
	    	bullProbability.up(_confidence, _time);
    	} else if(_direction == MarketModelEvent.MARKET_GOING_DOWN){
	    	bearProbability.up(_confidence, _time);
	    	bullProbability.down(_confidence, _time);
    	} else if(_direction == MarketModelEvent.MARKET_STALL){
	    	bearProbability.down(_confidence, _time);
	    	bullProbability.down(_confidence, _time);
	    }
//    	debug.outln(Debug.INFO, "computeMarketProbabilities.MarketModelEvent.out: bearProbability="+bearProbability+", bullProbability="+bullProbability+", _marketModelEvent="+_marketModelEvent);
    }

//-------------------------------------------------------------------------------------
    public float getBearProbability(){
    	return bearProbability.get();
    }

//-------------------------------------------------------------------------------------
    public float getBullProbability(){
    	return bullProbability.get();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	bearProbability.load();
    	bullProbability.load();

    	List<Map<String, String>> _signalsNames = new ArrayList<Map<String, String>>();
    	Map<String, String> _signalNames = new HashMap<String, String>();
    	_signalNames.put("type", "whalealert");
    	_signalsNames.add(_signalNames);

    	CurrencyPair _currencyPair = new CurrencyPair("BTC/USDT");

    	resolver.getSignals()
    			.getSignals(_currencyPair, _signalsNames)
				.forEach(_signal -> {
					debug.outln(Debug.INFO, "Registering Autopilot for signal =["+_signal+"]...");
					_signal.getSignalEvent()
					.subscribeOn(Schedulers.computation())
	    			.observeOn(Schedulers.newThread(), false)
	    			.subscribe(_signalEvent -> onNextEvent(_signalEvent),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());
	    		});  

		resolver.getMarketModels()
			.getMarketModel(_currencyPair)
			.getMarketModelEvent()
			.subscribeOn(Schedulers.io())
			.observeOn(Schedulers.computation(), false)
			.subscribe(_marketModelEvent -> onNextEvent(_marketModelEvent),
						_throwable -> onError(_throwable),
    					() -> onCompleted());	
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
    	bearProbability.save();
    	bullProbability.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "MarketAnalyzer:";
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
