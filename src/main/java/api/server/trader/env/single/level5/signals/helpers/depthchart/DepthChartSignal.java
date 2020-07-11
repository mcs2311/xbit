//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.depthchart;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.text.*;

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

import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.helpers.*;
import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level5.signals.*;

//-------------------------------------------------------------------------------------
public class DepthChartSignal extends Signal {
//	private String exchange;
	private CurrencyPair repositoryCurrencyPair;

	private int levels;
	private float thresholdBuy;
	private float thresholdSell;


	private int eventCounter;

	//---statics:
	private final static DecimalFormat df = new DecimalFormat("#.##");

//-------------------------------------------------------------------------------------
    public DepthChartSignal(Debug _debug, Resolver _resolver, SignalConfiguration _configuration, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _configuration, _currencyPair, _args);

        eventCounter = 0;
		debug.outln(Debug.WARNING, "DepthChartSignal._args: "+args);

//		exchange = args.get("exchange");
		repositoryCurrencyPair = new CurrencyPair(args.get("currencyPair"));

		String _s = args.get("levels");
		if(_s != null){
			levels = Integer.parseInt(_s);
		}

		_s = args.get("thresholdBuy");
		if(_s != null){
			thresholdBuy = Float.parseFloat(_s);
		}

		_s = args.get("thresholdSell");
		if(_s != null){
			thresholdSell = Float.parseFloat(_s);
		}

    	load();
    }


//-------------------------------------------------------------------------------------
/*
	public void onNext(OrderbookEvent _orderbookEvent) {
    	long _bidsSize = _orderbookEvent.getBidsSize();
    	long _asksSize = _orderbookEvent.getAsksSize();
		float _ratio;

		if(_bidsSize >= _asksSize){
			_ratio = ((float)_bidsSize)/_asksSize;
		} else {
			_ratio = ((float)_asksSize)/_bidsSize;
		}

		float _confidence = (float)Math.log10(_ratio);


		long _marketSize = _bidsSize + _asksSize;
		if(_marketSize > maxMarketSize){
			maxMarketSize = _marketSize;
		}

		_confidence = _confidence * ((float)_marketSize/maxMarketSize);

		_confidence = (float)Math.min(_confidence, 0.95f);


		if(_confidence < 0.6f){
			return;
		}

		SignalEvent _newEvent = null;
		if(_bidsSize > _asksSize){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_SELL, _confidence, SignalEvent.SOURCE_DEPTHCHART);
	//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		} else {
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_BUY, _confidence, SignalEvent.SOURCE_DEPTHCHART);
//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		}


		debug.outln(Debug.WARNING, "DepthChartSignal.onNext: " + _marketModelEvent + ", _confidence=" + Float.valueOf(df.format(_confidence)));

		
		if(_newEvent != null){
			if(emitter != null){
				emitter.onNext(_newEvent);
			}
		}
	}
*/
//-------------------------------------------------------------------------------------
	public void onNext(MarketModelEvent _marketModelEvent) {
		BeanEvent _beanEvent = _marketModelEvent.getBeanEvent();
		if(!(_beanEvent instanceof OrderbookEvent)){
			return;
		}
		int _direction = _marketModelEvent.getDirection();
		float _confidence = _marketModelEvent.getConfidence();
/*
		if(_confidence < threshold){
			return;
		}
*/

		_confidence = _confidence * confidence;
		_confidence = Math.min(_confidence, 0.95f);

		SignalEvent _newEvent = null;
		if((_direction == MarketModelEvent.MARKET_GOING_UP) && (_confidence > thresholdBuy)){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_BUY, _confidence, SignalEvent.SOURCE_DEPTHCHART);
	//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		} else if((_direction == MarketModelEvent.MARKET_GOING_DOWN)  && (_confidence > thresholdSell)){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_SELL, _confidence, SignalEvent.SOURCE_DEPTHCHART);
//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		}



		
		if(_newEvent != null){
			if(emitter != null){
				debug.outln(Debug.WARNING, "DepthChartSignal.onNext: " + _marketModelEvent + ", _confidence=" + Float.valueOf(df.format(_confidence)));
				emitter.onNext(_newEvent);
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
/*    	OutputRepository _outputRepository = (OutputRepository)resolver.getRepositories()
    	.getOutputRepositories()
    	.getRepository(repositoryCurrencyPair, "orderbook", "instant");

    	if(_outputRepository == null){
    		debug.outln(Debug.ERROR, "Error 222: Cannot find repository with currencyPair= "+currencyPair);
//    		System.exit(0);
    		return;
    	}

    	((OrderbookRepository)_outputRepository).getBean(levels)
//				.onBackpressureDrop()
//				.observeOn(Schedulers.newThread(), false)
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.computation(), false)
		.subscribe(_orderbookEvent -> onNext(_orderbookEvent),
					_throwable -> onError(_throwable),
					() -> onCompleted());
*/

		resolver.getMarketModels()
			.getMarketModel(currencyPair)
			.getMarketModelEvent()
			.subscribeOn(Schedulers.io())
			.observeOn(Schedulers.computation(), false)
			.subscribe(_marketModelEvent -> onNext(_marketModelEvent),
						_throwable -> onError(_throwable),
    					() -> onCompleted());	

    }


//-------------------------------------------------------------------------------------
    public void save(){
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "DepthChartSignal:" + 
		currencyPair + 
		" , " + 
		args;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
