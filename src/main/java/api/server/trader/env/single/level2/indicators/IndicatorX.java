//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level2.indicators;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

//import rx.*;
import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.*;
import org.ta4j.core.trading.rules.*;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.helpers.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;

//-------------------------------------------------------------------------------------
public class IndicatorX extends AbstractEntity<Configuration> implements FlowableOnSubscribe<BeanEvent>/*, Subscriber<BeanEvent>*/ {
	private String indicatorName;
	private CurrencyPair currencyPair;
	private String barDuration;

    private BarSeries series;
	private CachedIndicator<Num> indicator;

	private Num lastIndicatorValue;
	private Num diffThreshold;
	private int eventCounter;

    //---rx:
	private Flowable<BeanEvent> flowable;
	protected FlowableEmitter<BeanEvent> emitter;


	//---cache:
	private OutputRepository outputRepository;

//-------------------------------------------------------------------------------------
    public IndicatorX(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, List<String> _indicator) {
    	super(_debug, _resolver, null, null);
    	currencyPair = _currencyPair;
    	indicatorName = _indicator.get(0);
    	barDuration = _indicator.get(1);
    	lastIndicatorValue = PrecisionNum.valueOf(0.0);
    	diffThreshold = PrecisionNum.valueOf(0.0);
		eventCounter = 0;

    	flowable = Flowable.create(this, BackpressureStrategy.DROP)
    				.share();
//    			.onBackpressureBuffer(16, () -> { },
//              		BackpressureOverflowStrategy.DROP_OLDEST);


    	outputRepository = (OutputRepository)resolver
		    	.getRepositories()
		    	.getOutputRepositories()
		    	.getRepository(currencyPair, "trade", barDuration);


    	if(outputRepository == null){
    		debug.outln(Debug.ERROR, "Cannot find repository with currencyPair= "+currencyPair+" and barDuration=" + barDuration);
    		return;
    	}

    	series = outputRepository.getSeries(null);
    	if(series == null){
    		debug.outln(Debug.ERROR, "Indicator... series is null");
    	}
    	createIndicator();
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void createIndicator() {
		if(series == null){
			debug.outln(Debug.ERROR, "IndicatorX.createIndicator series is null!!!");		
			System.exit(0);
		}
		debug.outln(Debug.INFO, "Create IndicatorX: "+indicatorName+", "+currencyPair+", "+barDuration);		
		switch(indicatorName){
			case "awsome_oscillator" : {
				indicator = new AwesomeOscillatorIndicator(series);
				break;
			}
			case "close_price" : {
				indicator = new ClosePriceIndicator(series);
				break;
			}
			case "high_price" : {
				indicator = new HighPriceIndicator(series);
				break;
			}
			case "low_price" : {
				indicator = new LowPriceIndicator(series);
				break;
			}
			case "hma" : {
//				indicator = new HMAIndicator(series);
				break;
			}
			default : {
				debug.outln(Debug.ERROR, "Could not find indicator named: " + indicatorName);
				System.exit(0);
				break;
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onNext(BeanEvent _beanEvent) {
//		System.exit(0);
		TradeEvent _tradeEvent = (TradeEvent)_beanEvent;
		int _index = _tradeEvent.getIndex();		
		if(_index > series.getEndIndex()){
			debug.outln(Debug.WARNING, "IndicatorX.onNext: Index greater than last index in series: " + _beanEvent+", seriesEndIndex:"+series.getEndIndex()+">>>"+this+", index =" + _index+", outputRepository="+outputRepository);
			System.exit(0);
			return;
		}

/*		if(eventCounter%2000 == 0){
			debug.outln("IndicatorX.onNext: " + _beanEvent);
		}
*/
		eventCounter++;

		Num _currentIndicatorValue = null;
		try{
			_currentIndicatorValue = indicator.getValue(_index);
		}catch(Exception _e){
			debug.outln(Debug.WARNING, "["+currencyPair.toString()+"]: Error processing event in IndicatorX: " + _beanEvent + ": " + _e.getMessage());
			_e.printStackTrace();
			System.exit(0);
			return;
		}
//		Num _diff = lastIndicatorValue.minus(_currentIndicatorValue).abs();
//		if(_diff.isGreaterThanOrEqual(diffThreshold)){
//			Event _newEvent = new Event(Event.INDICATOR_UPDATE, null, _beanEvent, _index);
//			pushEvent(_newEvent);
			if(emitter != null){
				emitter.onNext(_tradeEvent);
			}			
//		}
//			Event _newEvent = new Event(Event.INDICATOR_UPDATE, null, _beanEvent, _index);
//			pushEvent(_newEvent);

		lastIndicatorValue = _currentIndicatorValue;
		
	}



//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<BeanEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

/*
//-------------------------------------------------------------------------------------
	public void onSubscribe(Subscription _s) {
//    	_s.request(Long.MAX_VALUE);
	}
*/
//-------------------------------------------------------------------------------------
	public Flowable<BeanEvent> getBean() {
		return flowable;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public CachedIndicator<Num> getIndicator() {
		return indicator;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
		outputRepository.getBean()
				.subscribeOn(Schedulers.io())
//				.observeOn(Schedulers.computation())
//				.onBackpressureBuffer(16, () -> { },
//              		BackpressureOverflowStrategy.DROP_OLDEST)
//				.onBackpressureDrop()
				.observeOn(Schedulers.computation(), false)
//				.observeOn(Schedulers.newThread(), false)
    			.subscribe(_beanEvent -> onNext(_beanEvent),
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
		return "IndicatorX:" + 
		indicatorName + 
		" , " + 
		currencyPair +
		" , " +
		barDuration;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
